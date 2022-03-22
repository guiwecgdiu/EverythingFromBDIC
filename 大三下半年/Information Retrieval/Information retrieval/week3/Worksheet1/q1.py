

filename = "words.txt"

with open(filename) as file:
	for line in file:
		words = line.split()
		for word in words:
			print("({})".format(word), end="")
		print()