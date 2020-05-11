import os

print("LOCUST_FILE : {}".format(os.getenv("LOCUST_FILE")))
print("USER_COUNT : {}".format(os.getenv("USER_COUNT")))

locust_files = os.getenv("LOCUST_FILE").split(",")
user_counts= os.getenv("USER_COUNT").split(",")

total = 0

for user_count in user_counts:
    total += int(user_count)

print("Total necessary user count: {}".format(total))
