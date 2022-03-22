import porter

file = open("stopwords.txt", mode="r", encoding='UTF-8')
for line in file:
    print(line, end="")
  


# def q2(type=2):
#     file = read_()
#     for line in file:
#         line2 = line.split()
#         for word in line2:
#             if type == 3:
#                 word = word.lower()
#             print("({})".format(word), end="")
#         print()
file.close()




# p = porter.PorterStemmer()
# word = 'connecting'
# print( p.stem( word ) )