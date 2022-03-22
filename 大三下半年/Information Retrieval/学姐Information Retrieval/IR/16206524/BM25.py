import re
import math
import porter











#I am not a very clever student who finished every questions.
#I just did whatever I can
#I did not do the evaluation part because it is really beyond me and I did not have time to finish.
#I did the first part and successfully calculate similarity scores based on BM25 model
#

















idfs = {}
stopwords = set()
vectors={}
w= set()
tf = {}
k1 = 1.5
b = 0.75
calbm = {}
with open( 'stopwords.txt', 'r' ) as f:
    for line in f:
        stopwords.add(line.rstrip())

stemmer = porter.PorterStemmer()

with open("lisa.all.txt") as f:
    documents = f.read().split('********************************************')[:-1]
doc_dic = dict()
words_temp = dict()
sum = 0
s = 0
for doc in documents:
    freq = {}
    maxfreq = 0
    
    terms = doc.split('\n')
    doc_id = terms[1]
    
    words_dic = dict()
    tmp ={}
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
avg_doclen = sum / docNumber
while True:
    iquery = input("Please enter your query: ")
    if iquery.lower() == "quit":
        print("quit the program")
        break
    query=list()
    for w in iquery.split():
        w = w.lower()
        if w not in stopwords:
            w = stemmer.stem(w)
            query.append(w)
    print(query)

    ni = 0
    d=0
    for term in query:
        logg = 0
        ni = 0
        count = 0
        calBM = 0
        for doc in vectors:
            
            terms = doc.split('\n')
            doc_id = terms[1]
            for word in vectors[doc]:
                if word == term:
                    d = len(vectors[doc])
                    ni = ni+1
                    vectors[doc][word] += 1
                    fij = vectors[doc][word]
                    calBM += (fij * 2 / (fij + (0.25 + 0.75 * d / avg_doclen)))
            logg = math.log(docNumber - ni + 0.5) - math.log(ni + 0.5)
            calbm[doc_id] = calBM*logg
            #calculate the
            calBM = 0
            count = count +1

    sort = sorted(calbm.items(), key=lambda d: d[1], reverse=True)
    sort = sort[:15]

    i = 1
    while i<16:
        print("rank",i,":",sort[i-1])
        i = i+1


