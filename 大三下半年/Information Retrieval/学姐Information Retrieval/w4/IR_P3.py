import porter
import time


def read_(name="stopwords.txt"):
    file = open(name, mode="r", encoding='UTF-8')
    return file


if __name__ == '__main__':
    start_time = time.process_time()
    cache=dict()
    p = porter.PorterStemmer()
    file = read_()
    my_dict = dict()
    for line in file:
        my_dict[line[0: - 1]] = None
#    dict = {'a': 1, 'b': 2}
#    dict['b']=2
    file.close()
    file2 = read_("npl-doc-text.txt")
    word_list_list = list()
    word_list_list.append(list())
    num: int = 1
    for line in file2:
        line = line[:-1]
        if line.isnumeric():
            num = int(line)
            word_list_list.append(list())
        line2 = line.split()
        for word in line2:
            if not word.isnumeric() and word not in my_dict and word != "/":
                if word in cache:
                    cache[word]=p.stem(word)
                    word_list_list[num].append(cache[word])
                else:
                    word_list_list[num].append(p.stem(word))
    file2.close()
    num += 1
    word_list_dict = [{}]
    num2 = 0
    for word_list in word_list_list[1:]:
        num2 += 1
        # num2=word_list_list.index(word_list)
        # really slow!
        word_list_dict.append(dict())
        # print(str(num2) + "/11429")
        for word in word_list:
            if word not in word_list_dict[num2]:
                word_list_dict[num2][word] = 1
            else:
                word_list_dict[num2][word] += 1
    for i in range(1, len(word_list_dict)):
        word_list_dict[i] = sorted(word_list_dict[i].items(), key=lambda i: i[1], reverse=True)
        if word_list_dict[i][0][1] <= 1:
            continue
        print(str(i) + ":", end='')
        for o in word_list_dict[i]:
            if o[1] <= 1: break
            print("%s (%d)" % (o[0], o[1]), end='')
        print()
    end_time = time.process_time()
    print('Time is {} seconds'.format(end_time - start_time))
