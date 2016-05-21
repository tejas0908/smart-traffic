import os
import sys
import time

import numpy as np

import config

sys.path.append(os.path.join(config.faster_rcnn_path, 'lib'))

from model import Model
from fast_rcnn.test import im_detect
from fast_rcnn.nms_wrapper import nms
import fast_rcnn


class ObjectDetector(object):
    def __init__(self, CONF_THRESHOLD=None, NMS_THRESHOLD=0.5, gpu=True, gpuid=0):
        self.model = Model.get_faster_rcnn_model(gpu, gpuid)
        self.cat = Model.get_faster_rcnn_categories()
        fast_rcnn.config.cfg.TEST.HAS_RPN = True

        if CONF_THRESHOLD == None or CONF_THRESHOLD == []:
            CONF_THRESHOLD = 0.7 * len(self.cat)
        if type(CONF_THRESHOLD) == list and len(CONF_THRESHOLD) != len(self.cat):
            raise Exception(
                    'Invalid list CONF_THRESH. If it is not None or empty then it must be of length of number of categories')
        if (type(CONF_THRESHOLD) == float):
            CONF_THRESHOLD = [CONF_THRESHOLD] * len(self.cat)
        CONF_THRESHOLD = map(lambda x: 0.7 if x == 0 else x, CONF_THRESHOLD)
        self.CONF_THRESHOLD = CONF_THRESHOLD
        self.NMS_THRESHOLD = NMS_THRESHOLD

    def detect(self, image):
        '''

        :param image: Image from which the objects should be detected
        :param CONF_THRESH: list of confidence threshold for each category. if None or empty 0.7 will be the threshold of each category
                            if non-empty but zero for some of the entries then 0.7 will be threshold for zero threshold value categories.
        :param NMS_THRESH: bounding box threshold, lower means less repetition and higher with high repetition
        :return: returns list of tuples of bounding box and category name as ((x1,y1,x2,y2),cls_name)
        '''
        start = time.time()
        bbox_class_list = []

        scores, boxes = im_detect(self.model, image)

        for cls_ind, (cls, threshold) in enumerate(zip(self.cat, self.CONF_THRESHOLD)):
            cls_ind += 1  # because we skipped background
            cls_boxes = boxes[:, 4 * cls_ind:4 * (cls_ind + 1)]
            cls_scores = scores[:, cls_ind]
            dets = np.hstack((cls_boxes,
                              cls_scores[:, np.newaxis])).astype(np.float32)
            keep = nms(dets, self.NMS_THRESHOLD)
            dets = dets[keep, :]
            inds = np.where(dets[:, -1] >= threshold)[0]

            for i in inds:
                # x1,y1,x2,y2 = dets[i,:-1]
                bbox_class_list.append(
                        {'bbox': dets[i, :-1].tolist(), 'category': cls, 'confidence': float(dets[i, -1])})
        end = time.time()

        return (bbox_class_list, end - start)
        # vis_detections(im, cls, dets, thresh=CONF_THRESH)

    @property
    def categories(self):
        return self.cat

    def get_nms_threshold(self):
        return self.NMS_THRESHOLD

    def set_nms_threshold(self, NMS_THRESHOLD):
        if NMS_THRESHOLD > 0 and NMS_THRESHOLD <= 1.0:
            self.NMS_THRESHOLD = NMS_THRESHOLD
        else:
            raise Exception('NMS Threshold must be between 1 and 0 including 1')

    nms_threshold = property(get_nms_threshold, set_nms_threshold,
                             doc="NMS_THRESHOLD must be between 0 and 1 including 1.")

    def get_conf_threshold(self):
        return self.CONF_THRESHOLD

    def set_conf_threshold(self, CONF_THRESH):
        if CONF_THRESH == None or CONF_THRESH == []:
            CONF_THRESH = 0.7 * len(self.cat)
        if len(CONF_THRESH) != len(self.cat):
            raise Exception(
                    'Invalid list CONF_THRESH. If it is not None or empty then it must be of length of number of categories')
        CONF_THRESH = map(lambda x: 0.7 if x == 0 else x, CONF_THRESH)
        self.CONF_THRESHOLD = CONF_THRESH

    conf_threshold = property(get_conf_threshold, set_conf_threshold,
                              doc="Confidenct threshold for detection. This must be list of threshold values for each category detection")
