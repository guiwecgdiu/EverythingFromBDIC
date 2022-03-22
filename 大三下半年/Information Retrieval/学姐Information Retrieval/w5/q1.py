import porter
documents = {
    'd1':['Shipment', 'of', 'gold', 'damaged', 'in', 'a', 'fire'], 
    'd2':['Delivery', 'of', 'silver', 'arrived', 'in', 'a', 'silver', 'truck'], 
    'd3':['Shipment', 'of', 'gold', 'arrived', 'in', 'a', 'large', 'truck']
}
query = ['gold', 'silver', 'truck']

term = {}
s = set()
dic = {}
p = porter.PorterStemmer()
with open('stopwords.txt','r') as f:
    for word in f:
        word = word.strip()
        s.add(word)
for k,v in documents.items():
    sdic = {}
    for value in v:
        value = value.lower()
        if value not in s:
            real = ''
            if value not in term:
                real = p.stem(value)
                term[value] = real
            else:
                real = term[value]
            if real not in sdic:
                sdic[real] = 1
    dic[k]=sdic
print(dic)

i = 0

for k,v in documents.items():
    for value in v:

        if value in query:
            i = i + 1
        
    
