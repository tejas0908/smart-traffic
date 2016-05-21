1. Create folder with the name of dataset
2. copy all the model files from this directory to newly created directory
3. modify the respective path in all the solver.pt
4. Modify num_classes to C;
5. Modify num_output in the cls_score layer to C
6. Modify num_output in the bbox_pred layer to 4 * C