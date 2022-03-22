def mergeNOT(list1, list2):
	answer=[]
	p1=0
	p2=0
	while p1 != len(list1) and p2 != len(list2):
		if list1[p1] == list2[p2]:
			p1+=1
			p2+=1
		elif list1[p1]<list2[p2]: 
			answer.append(list1[p1])
			p1+=1
		else:
			p2+=1
	return sorted(answer)

def mergeOR(list1, list2):
	answer=[]
	p1=0
	p2=0
	while p1 != len(list1) and p2 != len(list2):
		if list1[p1] == list2[p2]:
			answer.append(list1[p1])
		else:
			answer.append(list2[p2])
			answer.append(list1[p1])
		p1+=1
		p2+=1
	return sorted(answer)

def mergeAND(list1, list2):
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
	return answer

# q1.py
def read_from_file():
	dict={}	
	with open('index.txt') as i:
		for line in i:
			words = line.split()
			a=[]
			for item in words[1:]:
				# Make the doc ID as the int 
				item = int(item)
				a.append(item)
				# Sort the doc id
				a.sort()
			dict[words[0]]=a
	return dict

# search 
def searchFor(dict,words, Opearator="", word2=""):
	answer = []
	if Opearator.lower() == 'or':
		answer = mergeOR(dict[word1],dict[word2])
	elif Opearator.lower() == 'and':
		answer = mergeAND(dict[word1],dict[word2])
	else:
		answer = dict[word1]
	return answer






def calculate(words):
	words = message.split(" ")
	if message == 'keys':
		print(dict.keys())
	else:
		if len(words) == 3:
			print(searchFor(dict,words[0],words[1],words[2]))
		else:
			print(searchFor(dict,words[0]))


def run():
	dict = read_from_file()
	# input part
	prompt = "\nSearch for one term(Or for two terms using the AND and OR"
	prompt += "\noperators,separeted by space): "
	prompt += "\nEnter 'quit' to end the program."
	message = ""
	while message != 'quit':
		print(prompt)
		message = input(prompt)
		words = message.split(" ")
		if message == 'keys':
			print(dict.keys())
		else:
			if len(words) == 3:
				print(searchFor(dict,words[0],words[1],words[2]))
			else:
				print(searchFor(dict,words[0]))

# class value:
	


# find_all = lambda c, s: [x for x in range(c.find(s), len(c)) if c[x] == s]
# S = 'l(vel)et(co)de'
# C = ')'
# R = '('
# index_allR = find_all(S, C) # [3, 5, 6, 11]
# index_allL = find_all(S,R)
# match= dict(zip(index_allL,index_allR))

# for key, value in match.items():
# 	print(S[key+1:value])



Operator = ['NOT','OR','AND']
lang= "(data OR information) AND (radiation OR energy) AND (environment OR area)"
find_all = lambda c, s: [x for x in range(c.find(s), len(c)) if c[x] == s]
index_allR = find_all(lang,')')

print(index_allR)

result = "".join(lists)

print(result)
