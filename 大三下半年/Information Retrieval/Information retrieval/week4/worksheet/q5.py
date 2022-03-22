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


a = [1,2,3,4]
b = [3,4,5]

print(mergeNOT(a,b))