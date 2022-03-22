# q1.py

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
print(dict)