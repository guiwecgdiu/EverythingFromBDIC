# a.py
import porter
import math

p=porter.PorterStemmer()

documents = {
'd1':['Shipment', 'of', 'gold', 'damaged', 'in', 'a', 'fire'],
'd2':['Delivery', 'of', 'silver', 'arrived', 'in', 'a', 'silver', 'truck'],
'd3':['Shipment', 'of', 'gold', 'arrived', 'in', 'a', 'large', 'truck']
}

query = ['gold', 'silver', 'truck']

prep = {}

doc_v = {}

query_v = []
# stemming and stopword removal

def read_stopwords():
	stopwords = []
	filename= "stopwords.txt"
	with open(filename) as file:
		for line in file:
			line = line.strip()
			stopwords.append(line)
	return stopwords

def preprocess(doc, stopwords):
	collection=[]
	words = doc
	for word in words:
		if word not in stopwords and not word.isdigit():
			collection.append(p.stem(word).lower())
		else:
			pass
	return collection

def main():
	global documents
	global query
	global prep 
	global doc_v
	stopwords=read_stopwords()
	for k,doc in documents.items():
		prep[k]=preprocess(doc, stopwords)

	terms=[]
	terms=term(prep)
	
	for k,v in prep.items():
		p=0	
		vector=[]
		for p in terms:
			if p in prep[k]:
				vector.append(1)
			else:
				vector.append(0)	
		doc_v[k]=vector

	global query_v
	for p in terms:
		if p in query:
				query_v.append(1)
		else:
			query_v.append(0)	
#	print(query_v)
	similarity={}
	sort={}
	for k,v in doc_v.items():
		similarity[k]=sim(query_v,v)

	sort=dict(sorted(similarity.items(), key=lambda x:x[1], reverse=True))
	for k,v in sort.items():
		print(k+": {}".format(sort[k]))








def sim(q,d):
	answer=0
	answer = dot(q,d)/(length(q)*length(d))
	return answer

def dot(v1,v2):
	p1=0
	p2=0
	answer=0
	while p1 != len(v1) and p2 != len(v2):
		answer+=v1[p1]*v2[p2]
		p1+=1
		p2+=1
	return answer

def length(v):
	return math.sqrt(dot(v,v))



def term(prep):
	global documents
	global query
	terms=[]
	last = []
	for k,v in prep.items():
		if last != v:
			terms=mergeOR(last,v)
		last = v
	return terms


## OR operate array
def mergeOR(list1, list2):
	answer=[]
	p1=0
	p2=0
	while p1 != len(list1) and p2 != len(list2):
		if list1[p1] == list2[p2]:
			answer.append(list1[p1])
			p1=p1+1
			p2=p2+1
		elif list1[p1]<list2[p2]:
			p1=p1+1
		else: 
			p2=p2+1

	p1=0
	p2=0
	while p1 != len(list1) and p2 != len(list2):
		if list1[p1] not in answer:
			answer.append(list1[p1])
		if list2[p2] not in answer:
			answer.append(list2[p2])
		p1+=1
		p2+=1

	return sorted(answer)




main()