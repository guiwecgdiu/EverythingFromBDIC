with open('index.txt', 'r') as f:
    for line in f.readlines():
        line = line.split(' ')
        key= []
        for k in range(1,len(line)):
                key.append(int(line[k]))
        dict1[line[0]]=key
file.close()
