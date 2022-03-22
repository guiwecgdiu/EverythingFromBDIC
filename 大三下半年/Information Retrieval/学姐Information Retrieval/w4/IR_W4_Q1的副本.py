import porter
stopwords = set()

with open('stopwords.txt','r') as f:
    for line in f:
        stopwords.add(line.strip())#strip can remove space(tab, space, newe line)

print(stopwords)

#openNPL collection
with open('npl-doc-text.txt','r') as f:
    alldocs = f.read().split('    /')[0:-1]


print(alldocs)

#prepare for the processing
p = porter.PorterStemmer()
data={}

for doc in alldocs:
    print(doc)