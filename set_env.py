import os

def get_domain(test_env):
    if test_env in ["qca01", "qa", "ujetqa"]:
        domain = "cb.ujetqa.co"
    elif test_env in ["tst01", "pr", "ujetpr"]:
        domain = "cb.ujetpr.co"
    elif test_env in ["rel01", "rel02", "ujetrc"]:
        domain = "cb.ujetrc.co"
    elif test_env in ["stg01", "stg02", "ujetst"]:
        domain = "cb.ujetst.co"
    elif "prj" in test_env:
        domain = "prj{}.dev.ujet.xyz".format(os.getenv("TEST_COMPANY")[0:2])
    else:
        domain = "{}.ujet.co".format(test_env)
    return domain

print("LOCUST_FILE : {}".format(os.getenv("LOCUST_FILE")))
print("USER_COUNT : {}".format(os.getenv("USER_COUNT")))

locust_files = os.getenv("LOCUST_FILE").split(",")
user_counts= os.getenv("USER_COUNT").split(",")
total = 0

for user_count in user_counts:
    if len(user_count.strip()) == 0:
        continue
    total += int(user_count)

print("Total necessary user count: {}".format(total))

os.environ["USER_COUNT"] = str(total)
os.environ["ADMIN_PORTAL_URL"] = "https://{}.{}".format(os.getenv("TEST_COMPANY")[2:], get_domain(os.getenv("TEST_ENV")))

print("Admin portal URL : {}".format(os.getenv("ADMIN_PORTAL_URL")))

