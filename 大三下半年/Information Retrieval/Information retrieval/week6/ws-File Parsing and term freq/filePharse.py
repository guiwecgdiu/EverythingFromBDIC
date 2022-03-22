import porter
import time

p=porter.PorterStemmer()


def read_stopwords():
	stopwords = []
	filename= "stopwords.txt"
	with open(filename) as file:
		for line in file:
			line = line.strip()
			stopwords.append(line)
	return stopwords



def read_document():
	documents = {}
	filename = "npl-doc-text.txt"
	with open(filename) as file:
		doc = ""
		index = 1
		for line in file:
			if "/" in line:
				documents[index]=doc
				index=index+1
				doc=""
			else:
				doc+=line
		documents.popitem()
	return documents
	

def preprocess(doc, stopwords):
	collect=[]
	avoid = ['/']
	words = doc.split()
	for word in words:
		if word not in stopwords and word not in avoid and not word.isdigit():
			collect.append(p.stem(word))
		else:
			pass
	return collect


def account_freq(collect):
	dict = {}
	for word in collect:
		if word in dict.keys():
			dict[word]+=1
		else:
			dict[word]=1
	return dict

def sort_dic(dic):
	tuple= sorted(dic.items(), key=lambda x:x[1], reverse=True)
	return dict(tuple)


start_time=0
end_time = 0
def start():
	global start_time
	global end_time
	start_time = time.process_time()
	
	documents=read_document()
	

	for k,v in documents.items():
		
		stopwords= read_stopwords()
	
		# print(documents)
		collect=preprocess(v, stopwords)
		sortdic=sort_dic(account_freq(collect))
		term_fq =""
		for t,f in sortdic.items():
			if f>=2:
				term_fq+=" {} ({})".format(t,f)
		# print('{}:{}'.format(k,term_fq))
		end_time = time.process_time()


start()
duration = end_time-start_time 
print( 'Time is {} seconds'.format(duration) );





