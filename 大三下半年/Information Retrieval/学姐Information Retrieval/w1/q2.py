
with  open("words.txt", mode="r") as file:
    mydict = {}  # euqal to mydict=dict()
    context = re.split("\s*|\n*", file.read())
    for word in context:
            if word == "":
                continue
            word = word.lower()
            if word in mydict:
                mydict[word] += 1
            else:
                mydict[word] = 1
#    if (type == 5):
#        mydict2 = sorted(mydict.items(), key=lambda i: i[1], reverse=True)
#        for word in mydict2:
#            print("{}: {}".format(word[0], word[1]))
#        else:
    for word in mydict:
                print("{}: {}".format(mydict[word], word))
