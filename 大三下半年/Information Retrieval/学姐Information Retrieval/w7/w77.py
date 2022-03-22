import porter
import time
import math
import pickle
import os


def get_stop_list(file):
    return set([line.strip() for line in open(file)])


def get_document(file):
    with open(file) as f:
        documents = f.read().split('\n   /\n')[:-1]
    return documents


def get_doc_dic():
    documents = get_document(doc_file)
    doc_dic = dict()
    words_temp = dict()
    for doc in documents:
        terms = doc.split()
        doc_id = terms[0]
        words_dic = dict()
        for word in terms[1:]:
            if word not in stopwords_list:
                # result.append(p.stem(word))
                if word not in words_temp:
                    words_temp[word] = p.stem(word)
                word = words_temp[word]
                if word in words_dic:
                    words_dic[word] += 1
                else:
                    words_dic[word] = 1
        doc_dic[doc_id] = words_dic
    return doc_dic


def get_fre_seq(seq):
    terms_seq = dict()
    for w in seq:
        if w not in terms_seq:
            terms_seq[w] = 1
        else:
            terms_seq[w] += 1
    return terms_seq


def get_fre_gate(gate, doc_dic):
    tmp_docs_dic = dict()
    for k, v in doc_dic.items():
        tmp_doc_dic = dict()
        for k1, v1 in v.items():
            if v1 >= gate:
                tmp_doc_dic[k1] = v1
        tmp_docs_dic[k] = tmp_doc_dic
    return tmp_docs_dic


def sort_dic(dic):
    result = dict()
    tmp = sorted(dic.items(), key=lambda d: d[1], reverse=True)
    for (k,v) in tmp:
        result[k] = v
    return result


def sort_doc_dic(doc_dic):
    result = dict()
    for k, v in doc_dic.items():
        result[k] = sort_dic(v)
    return result


def print_dic(dic):
    for k, v in dic.items():
        print(k, v)


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


def get_vector(doc, terms, terms_idf):
    vector_dic = dict()
    terms_tf_idf = cal_tf_idf(doc, terms_idf, cal_tf(doc))
    for word in doc:
        vector_dic[terms[word]] = terms_tf_idf[word]
    return vector_dic


def get_doc_vector(doc_dic_tmp, terms_tmp, terms_idf):
    doc_vector_temp = dict()
    for k in doc_dic_tmp.keys():
        doc_vector_temp[k] = get_vector(doc_dic_tmp[k], terms_tmp, terms_idf)
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
        # 'd1': ['He', 'washed', 'his', 'coat', 'in', 'New', 'York'],
        # 'd2': ['My', 'dog\'s', 'coat', 'was', 'washed', ' yesterday'],
        # 'd3': ['My', 'new', 'coat', 'is', 'very', 'very', 'warm']
        'd1': ['Shipment', 'of', 'gold', 'damaged', 'in', 'a', 'fire'],
        'd2': ['Delivery', 'of', 'silver', 'arrived', 'in', 'a', 'silver', 'truck'],
        'd3': ['Shipment', 'of', 'gold', 'arrived', 'in', 'a', 'large', 'truck']
    }
    # stopwords_list = ['he', 'his', 'in', 'was', 'is']
    query = ['gold', 'silver', 'truck']
    for k, v in doc_temp.items():
        temp = list()
        for w in v:
            if w.lower() not in stopwords_list:
                temp.append(p.stem(w.lower()))
        doc_temp[k] = get_fre_seq(temp)
    terms = get_terms(doc_temp)
    terms_idf = cal_idf(doc_temp)
    doc_vector = get_doc_vector(doc_temp, terms, terms_idf)
    temp = []
    for w in query:
        if w not in stopwords_list:
            temp.append(p.stem(w))
    query_vector = get_vector(get_fre_seq(temp), terms, terms_idf)
    print(query_vector)
    print(terms)
    print(doc_temp)
    print(doc_vector)
    print(query_vector)
    sort_sim = cal_sim_val(query_vector, doc_vector)
    print(sort_sim)


def cal_idf(docs):
    terms_idf = dict()
    N = len(docs)
    for v in docs.values():
        for w, fre in v.items():
            if w in terms_idf:
                terms_idf[w] += 1
            else:
                terms_idf[w] = 1
    for k, v in terms_idf.items():
        terms_idf[k] = math.log(N/v, 2)
        # print(k, terms_idf[k])
    return terms_idf


def cal_tf(seq_doc):
    terms_tf = dict()
    max_fre = -1
    for v in seq_doc.values():
        if v > max_fre:
            max_fre = v
    for k, v in seq_doc.items():
        terms_tf[k] = v/max_fre
        # print(k, terms_tf[k])
    return terms_tf


def cal_tf_idf(seq_doc, terms_idf, terms_tf):
    terms_tf_idf = dict()
    for k in seq_doc:
        terms_tf_idf[k] = terms_tf[k] * terms_idf[k]
    return terms_tf_idf
#算idf

if __name__ == '__main__':
    start_time = time.process_time()
    doc_file = 'npl-doc-text.txt'
    stop_words_file = 'stopwords.txt'
    doc_vector_file = "doc_vector_advanced.pickle"
    terms_file = "terms.pickle"
    doc_dic_file = 'npl-doc-dic.pickle'
    p = porter.PorterStemmer()
    stopwords_list = get_stop_list(stop_words_file)

    small_documents_test()
    if os.path.exists(doc_dic_file):
        with open(doc_dic_file, 'rb') as f:
            doc_dic = pickle.load(f)
    else:
        with open(doc_dic_file, 'wb') as f:
            doc_dic = get_doc_dic()
            pickle.dump(doc_dic, f)
    if os.path.exists(terms_file):
        with open(terms_file, "rb") as f:
            terms = pickle.load(f)
    else:
        with open(terms_file, "wb") as f:
            terms = get_terms(doc_dic)
            pickle.dump(terms, f)
    terms_idf = cal_idf(doc_dic)
    if os.path.exists(doc_vector_file):
        with open(doc_vector_file, "rb") as f:
            doc_vector = pickle.load(f)
    else:
        with open(doc_vector_file, "wb") as f:
            doc_vector = get_doc_vector(doc_dic, terms, terms_idf)
            pickle.dump(doc_vector, f)
    end_time = time.process_time()
    print('load Time is {} seconds'.format(end_time - start_time))
    user_query = input("Input your query:")
    query = list()
    for w in user_query.split():
        if w not in stopwords_list:
            w = p.stem(w)
            query.append(w)

    start_query_time = time.process_time()
    user_query_vector = get_vector(get_fre_seq(query), terms, terms_idf)
    print(query)
    user_sort = cal_sim_val(user_query_vector, doc_vector)
    user_sort = user_sort[:10]
    print("Using TF-IDF:", user_sort)
    # the code to time is here
    end_time = time.process_time()
    print('Query Time is {} seconds'.format(end_time-start_query_time))
    print('Total Time is {} seconds'.format(end_time-start_time))
