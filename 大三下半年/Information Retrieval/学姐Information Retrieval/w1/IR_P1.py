import re


def read_(name="words.txt"):
    file = open(
        "words.txt", mode="r", encoding='UTF-8')
    return file


def q1():
    file = read_()
    for line in file:
        print(line, end="")
    file.close()


def q2(type=2):
    file = read_()
    for line in file:
        line2 = line.split()
        for word in line2:
            if type == 3:
                word = word.lower()
            print("({})".format(word), end="")
        print()
    file.close()


def q3():
    q2(3)


def q4(type=4):
    with  read_() as file:
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
        if (type == 5):
            mydict2 = sorted(mydict.items(), key=lambda i: i[1], reverse=True)
            for word in mydict2:
                print("{}: {}".format(word[0], word[1]))
        else:
            for word in mydict:
                print("{}: {}".format(mydict[word], word))


def q5():
    q4(type=5)
