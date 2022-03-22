import porter
import math
import numpy
import scipy
import time

p = porter.PorterStemmer()


def read_(name="D:\\OneDrive - University College Dublin\\S6\\IR\\stopwords.txt"):
    file = open(name, mode="r", encoding='UTF-8')
    return file


abvalue_save = {}


def ab_value(vec: list, num=None):
    global abvalue_save
    if num in abvalue_save:
        return abvalue_save[num]

    re = 0
    for i in vec:
        re += i ** 2
    re = math.sqrt(re)
    abvalue_save[num] = re
    return re


def dot_mul(a: list, b: list):
    re = 0
    for i in range(len(a)):
        re += a[i] * b[i]
    return re


def query_fun(que_list):
    que_vec = []
    for i in vec_dict:
        if i in que_list:
            que_vec.append(1)
        else:
            que_vec.append(0)
    ans_dict = {}
    num = 0
    ab_value_que_vec = ab_value(que_vec)
    for i in vector:
        com_vec = vector[i]
        num += 1
        ans_dict[num] = dot_mul(que_vec, com_vec) / ab_value_que_vec / ab_value(com_vec, i)
    ans_dict = sorted(ans_dict.items(), key=lambda i: i[1], reverse=True)
    for i in range(0, min(10, len(ans_dict))):
        print(str(i) + ": " + str(ans_dict[i]))


vec_dict = {}
vector = {}


def preprocess(type=0):
    global vec_dict, vector
    file = read_()
    stop_dict = dict()
    documents = {'d1': ['Shipment', 'of', 'gold', 'damaged', 'in', 'a', 'fire'],
                 'd2': ['Delivery', 'of', 'silver', 'arrived', 'in', 'a', 'silver', 'truck'],
                 'd3': ['Shipment', 'of', 'gold', 'arrived', 'in', 'a', 'large', 'truck']}
    if type == 1:
        index = None
        cache = dict()
        file2 = read_("D:/OneDrive - University College Dublin/S6/IR/npl-doc-text.txt")
        documents = dict()
        for line in file2:
            line = line[:-1]
            if line.isnumeric():
                index = line
                documents[index] = []
            else:
                line2 = line.split()
                for word in line2:
                    if not word.isnumeric() and word not in stop_dict and word != "/":
                        if word not in cache:
                            cache[word] = p.stem(word)
                            documents[index].append(cache[word])
                        else:
                            documents[index].append(cache[word])

    for line in file:
        stop_dict[line[0: - 1]] = None
    for i in documents:
        for o in range(len(documents[i]) - 1, -1, -1):
            documents[i][o] = p.stem(documents[i][o].lower())
            if documents[i][o] in stop_dict:
                documents[i].remove(documents[i][o])
            else:
                vec_dict[documents[i][o]] = None
    for i in documents:
        vector[i] = []
        for o in vec_dict:
            if o in documents[i]:
                vector[i].append(1)
            else:
                vector[i].append(0)
    for i in vector:
        ab_value(vector[i], i)

start_time = time.process_time()
preprocess(type=1)  # type=1 is the content in advanced question
end_time = time.process_time()
print('preprocess time is {} seconds'.format(end_time - start_time))
query = ['gold', 'silver', 'truck']
start_time = time.process_time()
query_fun(query)
end_time = time.process_time()
print('query time is {} seconds'.format(end_time - start_time))
