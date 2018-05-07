# ComplEx-NNE+AER

Codes and datasets for "Improving Knowledge graph embedding using simple constraints" (ACL-2018)
## Introduction
The repository provides java implementations and DB100K dataset used for the paper:
* [Improving Knowledge graph embedding using simple constraints](). Boyang Ding, Quan Wang, Bin Wang and Li Guo. ACL 2018.

As well as the implementations for the following papers:
* [Complex Embeddings for Simple Link Prediction](http://proceedings.mlr.press/v48/trouillon16.pdf). Théo Trouillon, Johannes Welbl, Sebastian Riedel, Éric Gaussier and Guillaume Bouchard. ICML 2016.
* [Regularizing Knowledge Graph Embeddings
via Equivalence and Inversion Axioms](https://luca.costabello.info/docs/ECML_PKDD_2017_embeddings.pdf). Pasquale Minervini, Luca Costabello, Emir Muñoz, Vít Nováček and Pierre-Yves Vandenbussche. ECML 2017. 
## Datasets
### Files
Datasets we used are in the corresponding subfolder contained in datasets/ with the following formats:
* _train.txt,_valid.txt,_test.txt; training, valid, test set with string id; format: **e1**\t**r**\t**e2**\n
* _cons.txt; approximate entailment constraints; formant: **r1,r2**\t**confidence**\n, where '-' denotes the inversion
### Preprocessing
```
python data.py data_folder
``` 
## Codes
### Run the code
```
java -jar -train data_folder/train.txt -valid data_folder/valid.txt -test data_folder/test.txt
```
### Parameters
You can changes parameter when training the model
```
k = number of dimensions
lmbda = L2 regularization coffecient
neg = number of negative samples
mu = AER regularization coffecient
```
## Citation
```
@inproceedings{boyang2018:aer,
	author = {Ding, Boyang and Wang, Quan and Wang, Bin and Guo, Li},
	booktitle = {56th Annual Meeting of the Association for Computational Linguistics},
	title = {Improving Knowledge Graph Embedding Using Simple Constraints},
	year = {2018}
}
```
## Contact
For all remarks or questions please contact Quan Wang: wangquan (at) iie (dot) ac (dot) cn .