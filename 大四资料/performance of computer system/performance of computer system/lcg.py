from time import time,clock

#define
m = 2**32
a = 1103515245
c = 12345
rdls = []

def LCG(seed,mi,ma,n):
    if n == 1:
        return 0
    else:
        seed = (a * seed + c) % m
        rdls.append(str(int((ma-mi)*seed/float(m-1)) + mi))
        LCG(seed,mi,ma,n-1)
          
def main():
    br = input("Please enter the bound of random number u want(seperate by ,):")
    co = eval(input("Please enter the number of random number you want:"))
    mi = eval(br.split(',')[0])
    ma = eval(br.split(',')[1])
    
    seed = time()
    
    LCG(seed,mi,ma,co)
    print("Random numbers:",rdls)
    r = ','.join(rdls)
    import os
    f = open('lcg.txt','w')
    f.write(r)

main()
