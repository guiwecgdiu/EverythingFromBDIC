import math
def precision(ret, rel):
    correct = 0
    for r in ret:
        if r in rel and rel[r]:
            correct += 1
    return correct / len(ret)


def get_relevant_set(rel):
    rel_temp = set()
    for k, v in rel.items():
        if v:
            rel_temp.add(k)
    return rel_temp


def recall(ret, rel):
    rel_temp = get_relevant_set(rel)
    correct = 0
    for r in ret:
        if r in rel_temp:
            correct += 1
    return correct/len(rel_temp)


def p_at_n(ret, rel, n):
    correct = 0
    for i in range(n):
        if ret[i] in rel and rel[ret[i]]:
            correct += 1
    return correct / n


def r_per(ret, rel, r):
    rel_temp = get_relevant_set(rel)
    correct = 0
    for e in ret:
        if correct / len(rel_temp) < r and e in rel and rel[e]:
            correct += 1
        if correct / len(rel_temp) >= r:
            return correct / (ret.index(e) + 1)


def mean_avg_per(ret, rel):
    per, correct = 0, 0
    for i in range(len(ret)):
        if ret[i] in rel and rel[ret[i]]:
            correct += 1
            per += (correct / (i+1))
    return per / len(get_relevant_set(rel))


def b_pref(ret, rel):
    pref, relevant_count, non_relevant = 0, 0, 0
    for r in ret:
        if r in rel and rel[r]:
            relevant_count += 1
    for r in ret:
        if r in rel and not rel[r]:
            non_relevant += 1
        if r in rel and rel[r]:
            pref = pref + max((1 - non_relevant / relevant_count), 0)
    return pref / relevant_count


def p_at_n(ret, rel, n):
    correct = 0
    for i in range(n):
        if ret[i] in rel and rel[ret[i]]:
            correct += 1
    return correct / n


def cal_dcg(ret, rel, n):
    dcg = []
    for i in range(n):
        if not i:
            if ret[i] in rel:
                dcg.append(rel[ret[i]])
            else:
                dcg.append(0)
        else:
            if ret[i] in rel:
                dcg.append(rel[ret[i]] / math.log(i+1, 2) + dcg[i - 1])
            else:
                dcg.append(dcg[i - 1])
    return dcg[n-1]


def n_dcg_at_n(ret, rel, n):
    i_ret = []
    dcg = cal_dcg(ret, rel, n)
    rel_sorted = sorted(rel.items(), key=lambda v: v[1], reverse=True)
    for (k, v) in rel_sorted:
        i_ret.append(k)
    i_dcg = cal_dcg(i_ret, rel, n)
    return dcg / i_dcg


retrieved = ['d12', 'd1', 'd19', 'd15', 'd2', 'd4', 'd8', 'd9', 'd6', 'd14', 'd3', 'd5', 'd16', 'd18', 'd13', 'd20',
             'd7', 'd11']
relevant = {'d1': 3, 'd3': 2, 'd7': 1, 'd10': 3, 'd11': 2, 'd16': 3, 'd17': 2, 'd18': 1, 'd2': 0, 'd6': 0, 'd8': 0,
            'd12': 0, 'd13': 0, 'd14': 0, 'd15': 0, 'd20': 0}
print("The precision is:", precision(retrieved, relevant))
print("The recall is:", recall(retrieved, relevant))
print("The p@10 is:", p_at_n(retrieved, relevant, 10))
print("The R-Precision @ R=30% is:", r_per(retrieved, relevant, 0.3))
print("The Mean Average Precision(MAP) is:", mean_avg_per(retrieved, relevant))
print("The bPref is:", b_pref(retrieved, relevant))
print("The NDCG@10 is:", n_dcg_at_n(retrieved, relevant, 10))
