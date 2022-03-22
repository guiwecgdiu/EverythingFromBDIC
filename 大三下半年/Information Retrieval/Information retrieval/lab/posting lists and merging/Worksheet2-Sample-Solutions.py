#!/usr/bin/env python3

###
#
# Sample solution for Worksheet 2
#
###

filename = 'index.txt'

# merge two postings lists, return the INTERSECTION
#    i.e. those documents that are in list1 AND in list2
def mergeAND(list1, list2):
	i, j = 0 , 0
	merged = []
	while i < len(list1) and j < len(list2):
		if list1[i] < list2[j]:
			i += 1
		elif list1[i] > list2[j]:
			j += 1
		else:
			merged.append(list1[i])
			i += 1
			j += 1
	return merged

# merge two postings lists, return the UNION
#    i.e. those documents that are in list1 OR in list2
#         each document should be in the output only once
def mergeOR(list1, list2):
	i, j = 0, 0
	merged = []
	while i < len(list1) or j < len(list2):
		if i < len(list1) and j == len(list2):
			merged.append(list1[i])
			i += 1
		elif j < len(list2) and i == len(list1):
			merged.append(list2[j])
			j += 1
		elif  list1[i] < list2[j]:
			merged.append(list1[i])
			i += 1
		elif list2[j] < list1[i]:
			merged.append(list2[j])
			j += 1
		else:
			merged.append(list2[j])
			i += 1
			j += 1
	return merged

# merge two postings lists, return the DIFFERENCE
#    i.e. those documents that are in list 1 but are NOT in list2.

# This is mentioned in the ``advanced'' section.

# def mergeNOT(list1,list2):
# 	i, j = 0 , 0
# 	merged = []
# 	while i < len(list1) and j < len(list2):
# 		if list1[i] < list2[j]:
# 			merged.append(list1[i])
# 			i += 1
# 		elif list1[i] > list2[j]:
# 			j += 1
# 		else:
# 			i += 1
# 			j += 1
# 	while i < len(list1):
# 		merged.append( list1[i] )
# 		i += 1
# 	return merged

# first read in the file and store in a data structure
#   dictionary where:
#      keys are terms
#      values are lists of docids (i.e. a postings list)

index = {}

with open(filename, 'r') as f:
    for line in f:
    	tokens = line.split()
    	index[ tokens[0] ] = [ int(x) for x in tokens[1:]]

query = input('Enter a query (one term, or two terms with AND, OR or NOT): ');

query_terms = query.split()

print( 'Searching for: [{}]...'.format(query) )

if len(query_terms) == 1:
	if query_terms[0] in index:
		print( ' '.join( [ str(x) for  x in index[query_terms[0]] ] ) )
	else:
		print( 'NOT FOUND' )
elif len(query_terms) == 3 and query_terms[1] == 'AND':
		print( ' '.join( [ str(x) for  x in mergeAND(index[query_terms[0]], index[query_terms[2]] ) ] ) )

elif len(query_terms) == 3 and query_terms[1] == 'OR':
		print( ' '.join( [ str(x) for  x in mergeOR(index[query_terms[0]], index[query_terms[2]] ) ] ) )
else:
	print('Invalid query')
	