
# Li Jiadi 17205985
# Information Retrieval Assignment


import os


import math

import re

import json

#different path
from optparse import OptionParser

parser = OptionParser()
parser.add_option("-m","--option")
options,args= parser.parse_args()
option_dict = vars(options)
mode=option_dict['option']


root_dir=os.getcwd()
document_folder= root_dir + '\\documents'
files_folder= root_dir + '\\files' + '\\'
document_prefix= document_folder + '\\'



import sys
sys.path.append(files_folder)
import porter


#############################
#		Evaluation			#
#		Method				#
#############################
#evaluation
def precision(ret,rel):
	correct=0
	try:
		for r in ret:
			if r in rel and rel[r]:
				correct+=1
		return correct/len(ret)
	except:
		pass


def recall(ret,rel):
	correct=0
	try:
		for r in ret:
			if r in rel and rel[r]:
				correct+=1
			return correct/len(rel)
	except:
		pass
		
def P_at_10(ret,rel):
	correct=0
	l=1
	try:
		for r in ret :
			if r in rel and rel[r]:
				correct+=1
			l+=1
			if(l>10):
				break
		return correct/10
	except:
		pass
		
def R_precision(ret,rel):
	correct=0
	l=1
	try:
		for r in ret:
			if r in rel and rel[r]:
				correct+=1
			l+=1
			if l>len(rel):
				break
		return correct/len(rel)
	except:
		pass


def MAP(ret,rel):
	per=0
	correct=0
	l=1
	try:
		for r in ret:
			if r in rel and rel[r]:
				correct+=1
				per+=(correct/l)
			l+=1
		return per/len(rel)
	except:
		pass

def b_pref(ret,rel):
	pref=0
	for r in ret :
		if r in rel and rel[r]:
			r_count+=1
	for r in ret:
		if r not in rel:
			n_count+=1
		if r in rel and rel[r]:
			pref = pref +(1 - n_count/ r_count)
	return pref/r_count


#############################
#		Evaluation			#
#		Method				#
#############################
		
#stopwords
with open(files_folder + 'stopwords.txt', 'r') as f:
	stopwords=set(f.read().split())


######################
#	START
#	PART
#	ONE
######################


#precalculate
doc_len={}
idfs={}
vectors={}
queries={}
similarity={}
k=1
b=0.75
query_vectors={}






#store information in an external file
ex_vector= root_dir + '/files/vectors.txt'
ex_freq= root_dir + '/files/frequency.txt'
ex_length= root_dir + '/files/length.txt'
stemmer=porter.PorterStemmer()


if not os.path.exists(ex_vector):
	# When the first time the program runs, it will create an
	# appropriate index. so thet IR using the BM25 method to
	# to perform BM25 later.

	index={}

	# Generate the index for storing terms.
	files= os.listdir(document_folder)
	for file in files:
		if not os.path.isdir(file): 
			with open(document_prefix + file, 'r', encoding='UTF-8')  as f:
				doc=f.read()
				allwords=re.compile("[a-z|\']+|[\d.]+", re.I).findall(doc)
				allwords.insert(0,file)
				index[allwords[0]]= allwords[1:]


	# Perform Stemming, calculates Term-frequency and length
	Stems={}
	for doc_id in index:
		freq={}#key(term),value(frequency)
		doc_len[doc_id]=0
		for term in index[doc_id]:#iterate all terms
			term=term.lower()
			if term not in stopwords and term !='.':
				if term not in Stems:
					Stems[term]=stemmer.stem(term)
				term=Stems[term]
				#one term length+1
				doc_len[doc_id]+=1
				#have seen this term in this document before
				if term in freq:
					freq[term]+=1
				#first time have seen
				else:
					freq[term]=1
					#this term was in a previous document
					if term in idfs:
						idfs[term]+=1
					#we've never seen this term before
					else:
						idfs[term]=1
	#get fi,j
		vectors[doc_id]=freq

	# When first time the program runs, it writes the indexs(length,vectors and idfs)
	# into external file
	json_v=json.dumps(vectors)
	json_i=json.dumps(idfs)
	json_l=json.dumps(doc_len)
	file=open(ex_vector, 'w')
	file.write(json_v)
	file.close
	file=open(ex_freq, 'w')
	file.write(json_i)
	file.close
	file=open(ex_length, 'w')
	file.write(json_l)
	file.close
else:
	file = open(ex_vector, 'r')
	json_v = file.read()
	vectors = json.loads(json_v)
	file.close()
	file = open(ex_freq, 'r')
	json_i = file.read()
	idfs = json.loads(json_i)
	file.close()
	file = open(ex_length, 'r')
	json_l = file.read()
	doc_len = json.loads(json_l)
	file.close()
	

#calculate part of BM25
for term in idfs:
	idfs[term]=math.log((len(vectors)-idfs[term]+0.5)/(idfs[term]+0.5),2)

#calculate average lengths
sum_len=0
for doc_id in vectors:
	sum_len+=doc_len[doc_id]
avg_len=sum_len/len(vectors)

#input query		
if mode=="manual" or None:
	while True:
		iquery=""
		try:
			iquery=input("Enter the Query:")
		except:
			pass
		if iquery.lower() == 'quit':
			break
		else:
			query = iquery.lower()
			allwords=re.compile("[a-z|\']+|[\d.]+", re.I).findall(query)
			for term in allwords:
				if term not in stopwords:
					term = stemmer.stem(term)
					if term not in query_vectors:
						query_vectors[term]=1
					else:
						query_vectors[term]+=1

			######################################
			#		Calculate the query similairty
			#
			########################################
			# For sum in d, and k in query:
			for doc_id in vectors:
				sim=0
				for term in query_vectors:
					if term in vectors[doc_id]:
						sim+=(idfs[term] * vectors[doc_id][term] * (k + 1) / (vectors[doc_id][term] + k * (1 - b) + b * doc_len[doc_id] / avg_len))
					similarity[doc_id]=sim
			##
			#############################################33333333333333
			# Accept a query on the command line and return a list of the 15 most relevant documents,
			# according to the BM25 IR Model, sorted beginning with the highest similarity score. The
			# output should have three columns: the rank, the document’s ID, and the similarity score. A
			# sample run of the program is contained later in this document. The user should continue to
			# be prompted to enter further queries until they type “QUIT
			##############################################################
			sort = sorted(similarity.items(), key=lambda d: d[1], reverse=True)
			sort = sort[:15]
			i = 1
			while i < 16:
				print("rank", i, ":", sort[i - 1])
				i = i + 1

	#################################
	# END OF PART ONE				#
	#################################
else:
#open queries
	with open(files_folder + 'queries.txt', 'r') as f:
		corpus=f.read()
		ques=corpus.split('\n')
		for que in ques:
			allwords=re.compile("[a-z|\']+|[\d.]+", re.I).findall(que)
			queries[allwords[0]]=allwords[1:]

	for qid in queries:
		q_vectors={}
		sim_r={}#key:did,value:sim
		for term in queries[qid]:
			if term not in stopwords and term != '.':
				term = stemmer.stem(term)
				if term not in q_vectors:
					q_vectors[term]=1
				else:
					q_vectors[term]+=1
		query_vectors[qid]=q_vectors

		for doc_id in vectors:
			sim=0
			for term in query_vectors[qid]:
				if term in vectors[doc_id]:
					sim+=(idfs[term] * vectors[doc_id][term] * (k + 1) / (vectors[doc_id][term] + k * (1 - b) + b * doc_len[doc_id] / avg_len))
			sim_r[doc_id]=sim
		similarity[qid]=sim_r

	#i=1
	#for did in sorted(sim_r, key=sim_r.get, reverse=True)[:15]:
		#print('{} Q0 {} {} {} 17205981'.format(qid, did, i, sim_r[did]))
		#i=i+1

#out put the result of each query
	ex_freq= root_dir + '/files/output.txt'
	if not os.path.exists(ex_freq):
		with open(ex_freq, 'w') as f:
			for key_s in similarity:
				i=1
				for key2 in sorted(similarity[key_s], key=similarity[key_s].get, reverse=True)[:30]:
					f.writelines(str(key_s) + ' ' + 'Q0' + ' ')
					line= str(key2) + ' ' + str(i) + ' ' + str(similarity[key_s][key2]) + ' ' + '17205985'
					f.writelines(line)
					f.write('\n')
					i+=1
		
			
	ret={}
	rel={}
	queires_key=list(queries.keys())
	i=queires_key[0]
	temp=i
#read the output and qrels into ret,rel
	with open(files_folder + 'output.txt', 'r') as f:
		doc_freq = {}
		corpus_d=f.read()
		docs=corpus_d.split('\n')
		for doc in docs:
			allwords=doc.split()
			if len(allwords)==6:
				if allwords[0]==i:
					doc_freq[allwords[2]]=0
					temp=i
				else:
					i=allwords[0]
					ret[temp]=doc_freq
					doc_freq={}
	queires_key=list(queries.keys())
	i=queires_key[0]
	temp=i
	with open(files_folder + 'qrels.txt', 'r') as f:
		corpus_d=f.read()
		docs=corpus_d.split('\n')
		doc_freq={}
		for doc in docs:
			allwords=doc.split()
			if len(allwords)==4:
				if allwords[0]==i:
					doc_freq[allwords[2]]=allwords[3]
					temp=i
				else:
					i=allwords[0]
					rel[temp]=doc_freq
					doc_freq={}


	precision_sum=0
	recall_sum=0
	p10_sum=0
	r_p_sum=0
	map_sum=0
	bp_sum=0
	for indexNum in ret:
		try:
			precision_sum+=precision(ret[indexNum], rel[indexNum])
			recall_sum+=recall(ret[indexNum], rel[indexNum])
			p10_sum+=P_at_10(ret[indexNum], rel[indexNum])
			r_p_sum+=R_precision(ret[indexNum], rel[indexNum])
			map_sum+=MAP(ret[indexNum], rel[indexNum])
			bp_sum+=b_pref(ret[indexNum], rel[indexNum])
		except:
			pass
	print("Evaluation score:")
	print("Precision: "+str(precision_sum / len(ret)))
	print("Recall: "+str(recall_sum / len(ret)))
	print("P@10: "+str(p10_sum / len(ret)))
	print("R_precision: "+str(r_p_sum / len(ret)))
	print("map_sum: "+str(map_sum / len(ret)))
	print("b_pref: "+str(bp_sum / len(ret)))
