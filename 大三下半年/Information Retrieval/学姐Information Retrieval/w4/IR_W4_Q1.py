import porter
stopwords = set()

with open('stopwords.txt','r') as f:
    for line in f:
        stopwords.add(line.strip())#strip can remove space(tab, space, new line)

print(stopwords)

#openNPL collection and read the document
with open('npl-doc-text.txt','r') as f:
    alldocs = f.read().split('   /')[0:-1]#we know three space and a forward slash
    #分隔符已知，所以用split     end one before the end  only the last one is rubbish (因为最后一个分隔符后面的都是。。。)
#alldocs 是一个list

print(alldocs)

#prepare for the processing ,create the stemmer
p = porter.PorterStemmer()
data={}#initialise the data structure to count frequency later

#key : docid
#value :另一个字典(先对每一个文件都创建一个，再把它们按照docid 加入data字典中)
    #key：term
    #value：frequency


for doc in alldocs[0:1]:
    freq = {}
    terms = doc.split()
    docid = trms[0]
    for term in terms[1:]:
        if term not in stopwaords:
            term = p.stem(term)