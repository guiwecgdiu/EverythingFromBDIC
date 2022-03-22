import porter
import time
import math
import pickle
import os


def get_stop_list(file):
    return set([line.strip() for line in open(file)])#把stopwords全部存到一个set里面


def term_frequencies(frequencies_gate, doc_id):#计算频率
    words_dic = dict()
    for word in doc_dic[doc_id]:
        if word in words_dic:
            words_dic[word] += 1
        else:
            words_dic[word] = 1
    words_dic = sorted(words_dic.items(), key=lambda d: d[1], reverse=True)
    result = list()
    for temp in words_dic:
        if temp[1] >= frequencies_gate:
            result.append(temp)
    return result


def get_document(file):
    with open(file) as f:
        documents = f.read()
    documents = documents.split('\n   /\n')
    return documents


def get_dic():#把doc存在字典
    doc_dic = dict()
    words_temp = dict()
    for doc in documents[:-1]:
        temp = doc.split('\n')
        doc_id = temp[0]
        content = list()
        for line in temp[1:]:
            for word in line.split():
                content.append(word)
        result = list()
        for word in content:
            if word not in stopwords_list:
                # result.append(p.stem(word))
                if word in words_temp:
                    result.append(words_temp[word])
                else:
                    words_temp[word] = p.stem(word)
                    result.append(words_temp[word])
        doc_dic[doc_id] = result
    return doc_dic


def get_fre(fre_gate, length):
    for i in range(1, length):
        print(i, end=': ')
        term_result = term_frequencies(fre_gate, i)
        if term_result:
            print(term_result)
        else:
            print('Null')


def cal_length(vector):
    # temp
    return math.sqrt(len(vector))


def get_terms(doc_temp):
    terms = dict()
    index = 0
    for k, v in doc_temp.items():
        for w in v:
            if w not in terms:
                terms[w] = index
                index += 1
    return terms


def get_vector(doc, terms):
    vector_dic = dict()
    for word in doc:
        vector_dic[terms[word]] = 1
    return vector_dic


def get_doc_vector(doc_dic_tmp, terms_tmp):
    doc_vector_temp = dict()
    for k in doc_dic_tmp.keys():
        doc_vector_temp[k] = get_vector(doc_dic_tmp[k], terms_tmp)
    # doc_vector_temp["1"] = get_vector(doc_dic_tmp["1"], terms_tmp)
    return doc_vector_temp


def cal_up_value(v1, v2):
    up_val = 0
    for k1, v in v1.items():
        if k1 in v2:
            up_val += (v * v2[k1])
    return up_val


def cal_sim_val(query_vector, doc_vector):
    sim_val = dict()
    query_len = cal_length(query_vector)
    for k, v in doc_vector.items():
        doc_len = cal_length(v)
        up_val = cal_up_value(v, query_vector)
        sim_val[k] = up_val / (doc_len * query_len)
    sort_sim = sorted(sim_val.items(), key=lambda d: d[1], reverse=True)
    return sort_sim


def small_documents_test():
    doc_temp = {
        'd1': ['Shipment', 'of', 'gold', 'damaged', 'in', 'a', 'fire'],
        'd2': ['Delivery', 'of', 'silver', 'arrived', 'in', 'a', 'silver', 'truck'],
        'd3': ['Shipment', 'of', 'gold', 'arrived', 'in', 'a', 'large', 'truck']
    }
    query = ['gold', 'silver', 'truck']
    for k, v in doc_temp.items():
        temp = set()
        for w in v:
            if w not in stopwords_list:
                temp.add(p.stem(w.lower()))
        doc_temp[k] = temp
    terms = get_terms(doc_temp)
    doc_vector = get_doc_vector(doc_temp, terms)
    query_vector = get_vector(query, terms)
    print(terms)
    print(doc_temp)
    print(doc_vector)
    print(query_vector)
    sort_sim = cal_sim_val(query_vector, doc_vector)
    print(sort_sim)


if __name__ == '__main__':
    start_time = time.process_time()
    stop_words_file = 'stopwords.txt'
    doc_file = 'npl-doc-text.txt'
    doc_vector_file = "doc_vector_advanced.pickle"
    terms_file = "terms.pickle"
    p = porter.PorterStemmer()
    stopwords_list = get_stop_list(stop_words_file)
    documents = get_document(doc_file)
    doc_dic = get_dic()
    small_documents_test()
    if os.path.exists(terms_file):
        with open(terms_file, "rb") as f:
            terms = pickle.load(f)
    else:
        with open(terms_file, "wb") as f:
            terms = get_terms(doc_dic)
            pickle.dump(terms, f)
    if os.path.exists(doc_vector_file):
        with open(doc_vector_file, "rb") as f:
            doc_vector = pickle.load(f)
    else:
        with open(doc_vector_file, "wb") as f:
            doc_vector = get_doc_vector(doc_dic, terms)
            pickle.dump(doc_vector, f)
    end_time = time.process_time()
    print('load Time is {} seconds'.format(end_time - start_time))
    user_query = input("Input your query:")
    start_time = time.process_time()
    user_query_vector = get_vector(user_query.split(), terms)
    print(user_query_vector)
    user_sort = cal_sim_val(user_query_vector, doc_vector)
    user_sort = user_sort[:10]
    print(user_sort)
    # the code to time is here
    end_time = time.process_time()
    print('query Time is {} seconds'.format(end_time-start_time))
