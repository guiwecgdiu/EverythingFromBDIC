import porter
import time
import math


def get_stop_list(file):
    return set([line.strip() for line in open(file)])


def term_frequencies(frequencies_gate, doc_id):
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


def get_dic():
    doc_dic = dict()
    words_temp = dict()
    for doc in documents[:-1]:
        temp = doc.split('\n')
        doc_id = int(temp[0])
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
    length = 0
    for value in vector:
        length += math.pow(value, 2)
    return math.sqrt(length)


def get_terms(doc_temp):
    terms = set()
    for k, v in doc_temp.items():
        for w in v:
            if w not in terms:
                terms.add(w)
    return terms


def get_vector(doc, terms):
    vector = []
    for w in terms:
        if w in doc:
            vector.append(1)
        else:
            vector.append(0)
    return vector


def get_doc_vector(doc_dic, terms):
    doc_vector = dict()
    for k in doc_dic.keys():
        doc_vector[k] = get_vector(doc_dic[k], terms)
    return doc_vector


def cal_up_value(v1, v2):
    up_val = 0
    for (qv, vv) in zip(v1, v2):
        up_val += qv * vv
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

    p = porter.PorterStemmer()
    stopwords_list = get_stop_list('stopwords.txt')
    documents = get_document('npl-doc-text.txt')
    doc_dic = get_dic()
    small_documents_test()
    terms = get_terms(doc_dic)
    doc_vector = get_doc_vector(doc_dic, terms)
    user_query = input("Input your query:")
    print(user_query)
    user_query_vector = get_vector(user_query.split(), terms)
    user_sort = cal_sim_val(user_query_vector, doc_vector)
    user_sort = user_sort[:10]
    print(user_sort)
    # get_fre(2, 15)
    # the code to time is here
    end_time = time.process_time()
    print('Total Time is {} seconds'.format(end_time-start_time))
    # print(doc_dic)
    # print(documents)
    # print(stopwords_list)
