import os
import sys

import config

sys.path.insert(0, os.path.join(config.faster_rcnn_path, 'caffe-fast-rcnn', 'python'))
import caffe


class Model(object):
    rcnn_model = None
    rcnn_cat = None
    classifier_model = None
    classifier_cat = None

    @staticmethod
    def __load_model_and_categories__(modelpath, gpu=True, gpuid=0):
        if (gpu):
            caffe.set_mode_gpu()
            caffe.set_device(gpuid)
        else:
            caffe.set_mode_cpu()

        files = os.listdir(modelpath)
        model = None
        deploy = None
        for f in files:
            name, ext = f.split('.')
            f = os.path.join(modelpath, f)
            if (ext == 'caffemodel'):
                model = f
            if (ext == 'prototxt' or ext == 'pt'):
                deploy = f
            if (ext == 'txt'):
                cat = [c.strip() for c in open(f).readlines()]
        if model == None or deploy == None:
            raise Exception('No model or deploy file at {}'.format(config.rccn_model_path))
        model = caffe.Net(deploy, model, caffe.TEST)

        return (model, cat)

    @staticmethod
    def get_faster_rcnn_model(gpu=True, gpuid=0):
        '''
        :return: This function returns the caffe rcnn model
        '''
        if (Model.rcnn_model != None):
            return Model.rcnn_model
        Model.rcnn_model, Model.rcnn_cat = Model.__load_model_and_categories__(config.rccn_model_path, gpu, gpuid)
        return Model.rcnn_model

    @staticmethod
    def get_faster_rcnn_categories():
        '''
        :return: This function returns the list of categories which can be detected by faster rcnn model
        '''
        if (Model.rcnn_cat != None):
            return Model.rcnn_cat
        Model.rcnn_model, Model.rcnn_cat = Model.__load_model_and_categories__(config.rccn_model_path)
        return Model.rcnn_cat
