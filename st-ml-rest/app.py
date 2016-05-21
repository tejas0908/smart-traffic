import cStringIO as StringIO
import datetime
import logging
import optparse
import os
import sys
from functools import wraps, update_wrapper
from logging.handlers import RotatingFileHandler

import flask
import skimage
import tornado.httpserver
import tornado.wsgi
import werkzeug
from PIL import Image
from flask import Flask, jsonify, make_response
from flask import send_file

import config
from detect import ObjectDetector

sys.path.append(os.path.join(config.faster_rcnn_path, 'lib'))
import fast_rcnn

UPLOAD_FOLDER = '/tmp/object_detection_upload/'
ALLOWED_IMAGE_EXTENSIONS = set(['png', 'bmp', 'jpg', 'jpe', 'jpeg', 'gif'])

# Obtain the flask app object
app = Flask(__name__)


def nocache(view):
    @wraps(view)
    def no_cache(*args, **kwargs):
        response = make_response(view(*args, **kwargs))
        response.headers['Last-Modified'] = datetime.datetime.now()
        response.headers['Cache-Control'] = 'no-store, no-cache, must-revalidate, post-check=0, pre-check=0, max-age=0'
        response.headers['Pragma'] = 'no-cache'
        response.headers['Expires'] = '-1'
        return response

    return update_wrapper(no_cache, view)


@app.route('/')
@nocache
def index():
    # return flask.render_template('index.html', has_result=False)
    return app.send_static_file('index.html')


@app.errorhandler(404)
@nocache
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)


@app.errorhandler(500)
@nocache
def abort(error):
    return make_response(jsonify({'error': '%s' % error}), 500)


@app.route(UPLOAD_FOLDER + '<filename>/', methods=['GET'])
@nocache
def tmp_uploaded_img(filename):
    return send_file(os.path.join(UPLOAD_FOLDER, filename), mimetype='image/jpeg')


@app.route('/ctolabs/detection/api/v1.0/detect_url', methods=['GET'])
@nocache
def detect_url():
    imageurl = flask.request.args.get('imageurl', '')
    print logging.info('Image url {}'.format(imageurl))
    try:
        im = skimage.io.imread(imageurl)
        # string_buffer = StringIO.StringIO(
        #    urllib.urlopen(imageurl).read())
        # im = Image.open(string_buffer)
        filename_ = str(datetime.datetime.now()).replace(' ', '_') + \
                    werkzeug.secure_filename('')
        converted_filename = os.path.join(UPLOAD_FOLDER, filename_ + '_converted.jpeg')
        filename = os.path.join(UPLOAD_FOLDER, filename_) + '.jpeg'

        skimage.io.imsave(filename, im)

        #        im = Image.open(string_buffer,'PNG')
        #        im.save(string_buffer,'JPEG')
        #        imagefile = open(filename,'wb')
        #        imagefile.write(string_buffer)
        #        imagefile.close()
        logging.info('Image saved as %s' % filename)
    except Exception as err:
        # For any exception we encounter in reading the image, we will just
        # not continue.
        logging.info('URL Image open error: %s', err)
        return flask.render_template(
                'index.html', has_result=True,
                result=(False, 'Cannot open image from URL.')
        )

    logging.info('Image: %s', imageurl)
    # result = app.clf.classify_image(image)

    bbox_cls, time = app.detector.detect(im)
    logging.info('Detection took {} seconds'.format(time))
    result = {'result': bbox_cls, 'queryImage': imageurl}

    # result['query_image'] = filename


    return make_response(jsonify(result), 200)


# @app.route('/classify_upload', methods=['POST'])
@app.route('/ctolabs/detection/api/v1.0/detect_upload', methods=['POST'])
@nocache
def retrieve_upload():
    try:
        # We will save the file to disk for possible data collection.
        logging.info('Received image')
        logging.info('POST request %s\n' % flask.request.files)
        imagefile = flask.request.files['file']
        im = skimage.io.imread(imagefile.stream)
        filename_ = str(datetime.datetime.now()).replace(' ', '_') + \
                    werkzeug.secure_filename(imagefile.filename)

        filename = os.path.join(UPLOAD_FOLDER, filename_)
        imagefile.save(filename)
        logging.info('Saving to %s.', filename)
        # im = skimage.io.imread(imagefile)


    except Exception as err:
        logging.info('Uploaded image open error: %s', err)
        return flask.render_template(
                'index.html', has_result=True,
                result=(False, 'Cannot open uploaded image.')
        )

    bbox_cls, time = app.detector.detect(im)
    logging.info('Detection took {} seconds'.format(time))
    result = {'result': bbox_cls, 'queryImage': filename + '/'}
    return make_response(jsonify(result), 200)
    # return result


@app.route('/ctolabs/detection/api/v1.0/categories', methods=['GET'])
@nocache
def detector_categories():
    result = {'categories': app.detector.categories}
    return make_response(jsonify(result), 200)


@app.route('/ctolabs/detection/api/v1.0/update_confidence_threshold', methods=['GET', 'POST'])
@nocache
def update_confidence_threshold():
    confidence_threshold_dict = flask.request.get_json()
    conf_threshold = app.detector.conf_threshold
    cls2id = {cls: id for id, cls in enumerate(app.detector.categories())}
    result = {'status': 'Ok'}
    for k, v in confidence_threshold_dict.items():
        if v > 0 and v < 1.0:
            conf_threshold[cls2id[k]] = float(v)
    app.detector.conf_threshold = conf_threshold
    return make_response(jsonify(result), 200)


@app.route('/ctolabs/detection/api/v1.0/update_nms_threshold', methods=['GET'])
@nocache
def update_nms_threshold():
    nms_threshold = float(flask.request.args.get('nms', ''))
    result = {'status': 'OK'}
    if nms_threshold > 0 and nms_threshold <= 1.0:
        app.detector.nms_threshold = nms_threshold
    else:
        result[
            'status'] = 'Error setting nms threshold value. Please check the value is greater than 0 and les or equal to 1'

    return make_response(jsonify(result), 200)


@app.route('/ctolabs/detection/api/v1.0/confidence_threshold', methods=['GET'])
@nocache
def confidence_threshold():
    result = {'confidence_threshold': app.detector.conf_threshold}
    return make_response(jsonify(result), 200)


@app.route('/ctolabs/detection/api/v1.0/nms_threshold', methods=['GET'])
@nocache
def nms_threshold():
    result = {'nms_threshold': app.detector.nms_threshold}
    return make_response(jsonify(result), 200)


def embed_image_html(image):
    """Creates an image embedded in HTML base64 format."""
    image_pil = Image.fromarray((255 * image).astype('uint8'))
    # image_pil = image_pil.resize((256, 256))
    string_buf = StringIO.StringIO()
    image_pil.save(string_buf, format='JPEG')
    data = string_buf.getvalue().encode('base64').replace('\n', '')
    return data


# def embed_image_html(image):
#    """Creates an image embedded in HTML base64 format."""
#    image_pil = Image.fromarray((255 * image).astype('uint8'))
#    image_pil = image_pil.resize((256, 256))
#    string_buf = StringIO.StringIO()
#    image_pil.save(string_buf, format='png')
#    data = string_buf.getvalue().encode('base64').replace('\n', '')
#    return 'data:image/png;base64,' + data


def allowed_file(filename):
    return (
        '.' in filename and
        filename.rsplit('.', 1)[1] in ALLOWED_IMAGE_EXTENSIONS
    )


def start_tornado(app, port=5000):
    http_server = tornado.httpserver.HTTPServer(
            tornado.wsgi.WSGIContainer(app))
    http_server.listen(port)
    print("Tornado server starting on port {}".format(port))
    tornado.ioloop.IOLoop.instance().start()


def start_from_terminal(app):
    """
    Parse command line options and start the server.
    """
    parser = optparse.OptionParser()
    parser.add_option(
            '-d', '--debug',
            help="enable debug mode",
            action="store_true", default=False)
    parser.add_option(
            '-p', '--port',
            help="which port to serve content on",
            type='int', default=5000)
    parser.add_option(
            '-g', '--gpu',
            help="use gpu mode",
            action='store_true', default=False)
    parser.add_option(
            '-i', '--deviceid',
            help="device id of the gpu to use",
            action='store_true', default=0)

    parser.add_option(
            '-n', '--nms',
            help='Set the nms threshold for detection bounding boxes (between 0.0 to 1.0) lower the number less redundant boxes',
            # action='store_true',
            type='float',
            default=0.5)

    parser.add_option(
            '-c', '--conf',
            help='Set the confidence threshold for each categories. This can be configure with different value for each category via REST API',
            # action = 'store_true',
            type='float',
            default=0.7
    )

    opts, args = parser.parse_args()
    if opts.gpu:
        fast_rcnn.config.cfg.USE_GPU_NMS = True
        fast_rcnn.config.cfg.GPU_ID = opts.deviceid
    else:
        fast_rcnn.config.cfg.USE_GPU_NMS = False

    app.detector = ObjectDetector(opts.conf, opts.nms, opts.gpu, opts.deviceid)

    file_handler = RotatingFileHandler('object_detection_demo.log', maxBytes=1024 * 1024 * 100, backupCount=20)
    file_handler.setLevel(logging.INFO)
    formatter = logging.Formatter("%(asctime)s - %(name)s - %(levelname)s - %(message)s")
    file_handler.setFormatter(formatter)
    app.logger.addHandler(file_handler)
    if opts.debug:
        app.run(debug=True, host='0.0.0.0', port=opts.port)
    else:
        start_tornado(app, opts.port)


if __name__ == '__main__':
    logging.getLogger().setLevel(logging.INFO)
    if not os.path.exists(UPLOAD_FOLDER):
        os.makedirs(UPLOAD_FOLDER)
    start_from_terminal(app)
