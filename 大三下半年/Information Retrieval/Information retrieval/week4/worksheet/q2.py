


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
# Search for 'a' and 'b'


print("The AND result between 'a' and 'aa' is " + ','.join('%s' %item for item in  mergeAND(dict['a'],dict['aa'])))
