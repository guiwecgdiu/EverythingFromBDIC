import porter
import math
import time

p = porter.PorterStemmer()
abvalue_save = {}
vec_dict = {}
vector = {}


def read_(name="stopwords.txt"):
    file = open(name, mode="r", encoding='UTF-8')
    return file


def ab_value(vec: list, num=None):
    if num != None:
        global abvalue_save
        if num in abvalue_save:
            return abvalue_save[num]
    re = 0
    for i in vec:
        re += i ** 2
    re = math.sqrt(re)
    if num != None:
        abvalue_save[num] = re
    return re


def dot_mul(a: list, b: list):
    re = 0
    for i in range(len(a)):
        re += a[i] * b[i]
    return re


def dot_mul2(a: list, b: list):
    re = 0
    for i in range(len(a)):
        re += 1 * b[a[i]]
    return re


def query_fun(que_list):
    for i in range(len(que_list)):
        que_list[i] = p.stem(que_list[i])
    que_vec = []
    que_vec2 = []
    num = 0
    for i in vec_dict:
        if i in que_list:
            # 向量原始形式
            que_vec.append(1)
            # 列表索引形式
            que_vec2.append(num)
        else:
            que_vec.append(0)
        num += 1
    ans_dict = {}
    num = 0
    ab_value_que_vec = ab_value(que_vec)
    for i in vector:
        com_vec = vector[i]
        num += 1
        ans_dict[num] = dot_mul2(que_vec2, com_vec) / ab_value_que_vec / ab_value(com_vec, i)
        # ans_dict[num] = dot_mul(que_vec, com_vec) / ab_value_que_vec / ab_value(com_vec, i)
    ans_dict = sorted(ans_dict.items(), key=lambda i: i[1], reverse=True)
    for i in range(0, min(10, len(ans_dict))):
        print(str(i) + ": " + str(ans_dict[i]))


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
        file2 = read_("npl-doc-text.txt")
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
                vec_dict[documents[i][o]] = list()
                # first place store how many documents contain term
                vec_dict[documents[i][o]].append(0)
                # second place plan to store relative position
                # todo:
    # turn documents to dict pattern
    for i in documents:
        tdict = {}
        for o in documents[i]:
            if o in tdict:
                tdict[o] += 1
            else:
                tdict[o] = 1
        maxfreq = 0
        for o in tdict:
            if tdict[o] > maxfreq:
                maxfreq = tdict[o]
        tdict['maxfreq'] = maxfreq
        documents[i] = tdict
    # calculate how many documents contain term i
    for i in documents:
        for o in documents[i]:
            if o in vec_dict:
                vec_dict[o][0] += 1
    N = len(documents)
    for i in documents:
        vector[i] = []
        for o in vec_dict:
            # todo:still can optimize
            if o in documents[i]:
                tf = documents[i][o] / documents[i]['maxfreq']
                idf = math.log(N / vec_dict[o][0], 2)
                vector[i].append(tf * idf)
                pass
            else:
                vector[i].append(0)
    # for i in documents:
    #     vector[i] = []
    #     for o in vec_dict:
    #         if o in documents[i]:
    #             vector[i].append(1)
    #         else:
    #             vector[i].append(0)
    for i in vector:
        ab_value(vector[i], i)
    return documents


if __name__ == '__main__':
    start_time = time.process_time()
    documents = preprocess(type=1)  # type=1 is the content in advanced question
    end_time = time.process_time()
    print('preprocess time is {} seconds'.format(end_time - start_time))
    query = ['gold', 'silver', 'truck']
    start_time = time.process_time()
    query_fun(query)
    end_time = time.process_time()
    print('query time is {} seconds'.format(end_time - start_time))
    start_time = time.process_time()
    query_fun(query)
    end_time = time.process_time()
    print('query again time is {} seconds'.format(end_time - start_time))
