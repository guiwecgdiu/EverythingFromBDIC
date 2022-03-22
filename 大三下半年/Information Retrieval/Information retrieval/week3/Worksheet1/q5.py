

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

for item in sorted(dic.items(), key=lambda x:x[1], reverse=True):
	print(item)