import porter

file = open("stopwords.txt", mode="r", encoding='UTF-8')
relist = list()
index = 0
for line in file:
    line2 = line.split()
    for word in line2:
        word = word.lower()
        relist.append(word)
        
        print(relist[index])
        index += 1

file.close()

file1 = open("npl-doc-text.txt", mode="r", encoding='UTF-8')
dict1 = dict()

#relist = list1.copy()
#    index1 = index2 = 0
#    while index1 < len(relist) and index2 < len(list2):
#        if relist[index1] == list2[index2]:
#            relist.pop(index1)
#            index2 += 1
#        else:
#            if relist[index1] < list2[index2]:
#                index1 += 1
#            else:
#                index2 += 1



file1.close()
