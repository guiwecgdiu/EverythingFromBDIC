#!/usr/bin/env python3
##
#
# Sample solution to Worksheet 7.
#
# Runs a query against 3 simple short documents using vector model with TF-IDF weights.
#
##
import re
import math
import porter
query = ['new', 'silver', 'truck']
idfs = {} # key is term, value is IDF of that term
stopwords = set()
vectors={}
w= set()


with open( 'stopwords.txt', 'r' ) as f:
    for line in f:
        stopwords.add(line.rstrip())

# load the porter stemmer
stemmer = porter.PorterStemmer()

with open("lisa.all.txt") as f:
    documents = f.read().split('********************************************')[:-1]
doc_dic = dict()
words_temp = dict()
sum = 0
s = 0
f = [] # 列表的每一个元素是一个dict，dict存储着一个文档中每个词的出现次数
tf = {} # 储存每个词以及该词出现的文本数量
idf = {} # 储存每个词的idf值
for doc in documents:
    freq = {} # will be stored in 'vectors' later
    maxfreq = 0
    
    terms = doc.split('\n')
    doc_id = terms[1]
    
    words_dic = dict()
    tmp ={}
    #print(doc_id)
    for term in terms[2:]:
        words = term.split()
        for word in words:
            r = '[‘!"#$%&\'()*+,./:;<=>?@[\\]^_`{}|~]+'
            word=re.sub(r, '',word)
            sum = sum+1
            word = word.lower()
            #print(word)
            if word not in stopwords:
                word = stemmer.stem(word)
                w.add(word)
                tmp[word] = tmp.get(word,0)+1
                if word in freq:
                    freq[word] += 1 # update weight if it's in the document
                else:
                    freq[word] = 1
                    if word in idfs:
                        idfs[word] += 1
                    else:
                        idfs[word] = 1
                   
                   #print(word)
                if freq[word] > maxfreq:
                    maxfreq = freq[word]
            
        for k in tmp.keys():
            tf[k] = tf.get(k,0)+1
    vectors[doc] = freq

docNumber = int(doc_id.split()[1])
#sum = the total number of words in the whole collection
avg_doclen = sum / docNumber


print(freq)
k1 = 1
b = 0.75
def inition(docs):
    D = len(docs)
    avgdl = sum([len(doc)+ 0.0 for doc in docs]) / D
    print(D)
    print(avgdl)
    for doc in docs:
        tmp = {}
        for word in doc:
            tmp[word] = tmp.get(word, 0) + 1  # 存储每个文档中每个词的出现次数
        f.append(tmp)
        for k in tmp.keys():
            tf[k] = tf.get(k, 0) + 1
    for k, v in tf.items():
        idf[k] = math.log(D - v + 0.5) - math.log(v + 0.5)


def sim(doc, index):
    score = 0.0
    for word in doc:
        if word not in f[index]:
            continue
        d = len(document[index])
        score += (idf[word] * f[index][word] * 2 / (f[index][word] + (0.25 + 0.75 * d / avg_doclen)))
    return score

def simall(doc):
    scores = []
    for index in range(docNumber):
        score = sim(doc, index)
        scores.append(score)
    return scores


print(inition(documents))
