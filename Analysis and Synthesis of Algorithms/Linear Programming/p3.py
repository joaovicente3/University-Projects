from pulp import *

def read_input():
    t, p, max_capacity = map(int, input().split())

    toys_data = {}
    toys_vars = {}
    toys_in_packages = {i: [] for i in range(1, t + 1)}
    
    for i in range(1, t + 1):
        profit, capacity = map(int, input().split())
        toys_data[i] = {'profit': profit, 'capacity': capacity}
        toys_vars[i] = LpVariable(f"Toy_{i}", lowBound=0, upBound=capacity, cat='Integer')

    packages = {}
    packages_vars = {}
    for i in range(p):
        toy1, toy2, toy3, profit = map(int, input().split())
        package_toys = [toys_vars[toy1], toys_vars[toy2], toys_vars[toy3]]
        packages[i] = {'profit': profit, 'toys': package_toys}
        pack_upbound = min(toys_data[toy1]['capacity'], toys_data[toy2]['capacity'], toys_data[toy1]['capacity'])
        packages_vars[i] = LpVariable(f"Package_{i}", lowBound=0, upBound=pack_upbound, cat='Integer')

        toy_ids = [toy1, toy2, toy3]
        for toy_id in toy_ids:
            if toy_id in toys_in_packages:
                toys_in_packages[toy_id].append(i)
            else:
                toys_in_packages[toy_id] = [i]

    return t, p, max_capacity, toys_data, toys_vars, packages, packages_vars, toys_in_packages

t, p, max_capacity, toys_data, toys_vars, packages, packages_vars, toys_in_packages = read_input()

prob = LpProblem("Maximize_Profit", LpMaximize)

prob += lpSum(toys_data[i]['profit'] * toys_vars[i] for i in range(1, t + 1)) + \
        lpSum(packages[j]['profit'] * packages_vars[j] for j in range(p)), "Total_Profit"

prob += lpSum(toys_vars[i] for i in range(1, t + 1)) + 3 * \
        lpSum(packages_vars[package] for package in packages) <= max_capacity, "Total_Capacity"

for toy_id in range(1, t + 1):
    toy_capacity = toys_data[toy_id]['capacity']

    prob += toys_vars[toy_id] + lpSum(packages_vars[i] for i in range(p) if i in toys_in_packages[toy_id]) <= \
            toy_capacity, f"Toy_{toy_id}_Capacity"

prob.solve(GLPK_CMD(msg=0))

print(value(prob.objective))


