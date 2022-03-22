import re


def read_(name="D:\\OneDrive - University College Dublin\\S6\\IR\\index.txt"):
    file = open(name, mode="r", encoding='UTF-8')
    return file


def init():
    file = read_()
    my_dict = dict()
    for line in file:
        line2 = line.split()
        # print(len(line2))
        my_dict[line2[0]] = list()
        for i in range(1, len(line2)):
            my_dict[line2[0]].append(int(line2[i]))
    file.close()
    return my_dict


def mergeAND(list1, list2):
    relist = list()
    index1 = index2 = 0
    while index1 < len(list1) and index2 < len(list2):
        if list1[index1] == list2[index2]:
            relist.append(list1[index1])
            index1 += 1
            index2 += 1
        else:
            if list1[index1] < list2[index2]:
                index1 += 1
            else:
                index2 += 1
    return relist


def mergeOR(list1, list2):
    relist = list()
    index1 = index2 = 0
    while index1 < len(list1) or index2 < len(list2):
        if index1 == len(list1):
            relist.append(list2[index2])
            index2 += 1
            continue
        if index2 == len(list2):
            relist.append(list1[index1])
            index1 += 1
            continue
        if list1[index1] == list2[index2]:
            relist.append(list1[index1])
            index1 += 1
            index2 += 1
        else:
            if list1[index1] < list2[index2]:
                relist.append(list1[index1])
                index1 += 1
            else:
                relist.append(list2[index2])
                index2 += 1

    return relist


def search_simple(command=None, command2=None, ifprint=True):
    if command is None:
        command = input("input your query")
    if command2 is None:
        command2 = command.split()
    if command2[1].upper() == 'AND':
        print(mergeAND(my_dict[command2[0]], my_dict[command2[2]]))
    elif command2[1].upper() == 'OR':
        print(mergeOR(my_dict[command2[0]], my_dict[command2[2]]))
    else:
        print("syntax error!")


def search_complex(command=None):
    if command is None:
        command = input("input your query")
    # command2 = re.split()
    # todo


def mergeNOT(list1, list2):
    relist = list1.copy()
    index1 = index2 = 0
    while index1 < len(relist) and index2 < len(list2):
        if relist[index1] == list2[index2]:
            relist.pop(index1)
            index2 += 1
        else:
            if relist[index1] < list2[index2]:
                index1 += 1
            else:
                index2 += 1
    return relist


my_dict = init()
# print(mergeNOT([1,2,3,4,5,6],[2,4,8]))
# print(mergeAND([1, 2, 4], [2, 3, 4, 5]))
# print(mergeOR([1, 2, 3], [3, 4, 5]))
