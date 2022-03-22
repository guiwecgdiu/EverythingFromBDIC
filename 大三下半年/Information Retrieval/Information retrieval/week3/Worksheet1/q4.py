

filename = "words.txt"
dic = {}

with open(filename,'r') as file:
	for line in file:
		words = line.split()
		for word in words:
			word=word.lower()
			if word in dic:
				dic[word]=dic[word]+1
			else:
				dic[word]=1

ordered = []
for key, item in dic.items():
	ordered.append({key:item})
	print(str(item)+":"+key)
			