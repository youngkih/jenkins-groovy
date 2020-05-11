import os

def set_ap_url(env, company):
    if env in ["qca01", "qa", "ujetqa"]:
        url = "https://{}.cb.ujetqa.co".format(company)
    elif env in ["tst01", "pr", "ujetpr"]:
        url = "https://{}.cb.ujetpr.co".format(company)
    elif env in ["rel01", "rel02", "ujetrc"]:
        url = "https://{}.cb.ujetrc.co".format(company)
    elif env in ["stg01", "stg02", "ujetst"]:
        url = "https://{}.cb.ujetst.co".format(company)
    elif "prj" in env:
        url = "https://{}.prj{}.dev.ujet.xyz".format(company[2:], company[0:2])
    else:
        url = "https://{}.{}.ujet.co".format(company, env)
    os.environ["ADMIN_PORTAL_URL"] = url

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
set_ap_url(os.getenv("TEST_ENV"), os.getenv("TEST_COMPANY"))

print("Admin portal URL : {}".format(os.getenv("ADMIN_PORTAL_URL")))

