import codecs
import random
import sympy

'''

Code written by: Ajay Devjani

'''
def get_prime():
    bits = 16
    while 1:
        p = int(random.uniform((2 ** bits + 1), (2 ** (bits + 1))))
        if sympy.isprime(p):
            return p


def get_int_of_msg(msg):
    msg_chunks = []
    intno = []
    for index in range(0, len(msg), 3):
        msg_chunks.append(msg[index: index + 3])
    print("Message Chunks: ")
    print(msg_chunks)
    for i in range(len(msg_chunks)):
        intno.append(int("0x" + codecs.encode(msg_chunks[i]).hex(), 16))
    return intno


def find_gcd(num1, num2):
    if num1 == 0:
        return num2
    if num2 == 0:
        return num1
    if num1 == num2:
        return num1
    if num1 > num2:
        return find_gcd(num2, num1 % num2)
    return find_gcd(num1, num2 % num1)


def get_multiplicative_inverse(e=79967, phiN=12544339720):
    if find_gcd(e, phiN) != 1:
        return False
    a = 0
    b = 1
    orig_phiN = phiN
    while e > 1:
        mod = phiN // e
        mod = a - (mod * b)
        a = b
        b = mod
        mod = phiN % e
        phiN = e
        e = mod
    if b < 0:
        b = b + orig_phiN
    return b


def get_exp_sq_n_mul(x, y, N):
    exp_bin = bin(y)[3:]
    value = x
    for i in exp_bin:
        value *= value
        if i == "1":
            value *= x
        value %= N
    return value


def encrypt_message(msg, e=2147483647, N=2909502443):
    int_chunks = get_int_of_msg(msg)
    cipher_text = []
    for i in int_chunks:
        i = i % N
        cipher_text.append(get_exp_sq_n_mul(i, e, N))
    return cipher_text


def get_ascii_from_int(msg):
    msg_str = ""
    msg_chunks = []
    for i in msg:
        msg_str += (bytes.fromhex(hex(i)[2:]).decode("ASCII"))
        msg_chunks.append((bytes.fromhex(hex(i)[2:]).decode("ASCII")))
    print("Decrypted Chunks:")
    print(msg_chunks)
    return msg_str


def decrypt_message(cipher_text):
    plain_text_chunks = []
    for i in cipher_text:
        i = int(i)
        plain_text_chunks.append(get_exp_sq_n_mul(i, 1664222743, 12544564267))
    return get_ascii_from_int(plain_text_chunks)


def sign(msg="Ajay Devjani", N=12544564267, d=1664222743):
    if len(msg) > N:
        return False
    int_chunks = get_int_of_msg(msg)
    signed_msg = []
    for i in int_chunks:
        i = i % N
        signed_msg.append(get_exp_sq_n_mul(i, d, N))
    return signed_msg


def verify_sign(signed_msg, msg, N=12544564267, e=79967):
    ver_msg = []
    for i in signed_msg:
        i = int(i)
        e = int(e)
        N = int(N)
        ver_msg.append(get_exp_sq_n_mul(i, e, N))
    if get_ascii_from_int(ver_msg) == msg:
        return True
    return False


def menu():
    print("Enter option to continue:\n")
    print("1. Display parameters.")
    print("2. Generate a prime number.")
    print("3. Encrypt message.")
    print("4. Decrypt message.")
    print("5. Sign Message.")
    print("6. Verify Signature.")
    print("7. Get Multiplicative Inverse.")
    print("\nEnter anything else to Exit.")
    choice = input()
    if choice == "1":
        print("Below are the parameters:\n\n")
        print("p = 12007")
        print("q = 104471")
        print("N = 12544564267")
        print("d = 1664222743")
        print("e = 79967")
        print("phiN = 12544339720")
    elif choice == "2":
        print("Generated prime number is: " + str(get_prime()))
    elif choice == "3":
        N = input(" Enter N : ")
        N = 2909502443 if N == "" else N
        e = input(" Enter e: ")
        e = 2147483647 if e == "" else e
        msg = input(" Enter message: ")
        print("\nEncrypted message: ")
        print(encrypt_message(msg, int(e), int(N)))
    elif choice == "4":
        cipher = input("\nEnter cipher chunks: ")
        print("Decrypted message: ")
        print(decrypt_message(cipher[1:-1].split(",")))
    elif choice == "5":
        msg = input("Enter message to sign (Default value: Ajay Devjani): ")
        msg = "Ajay Devjani" if msg == "" else msg
        print(sign(msg))
    elif choice == "6":
        signed_chunks = input("Enter Signed Chunks: ")
        N = input("Enter N: ")
        e = input("Enter e: ")
        msg = input("Enter message: ")
        N = 2909502443 if N == "" else N
        e = 2147483647 if e == "" else e
        msg = "Ajay Devjani" if msg == "" else msg
        print(verify_sign(signed_chunks[1:-1].split(","), msg, N, e))
    elif choice == "7":
        print("\nPlease provide value of a and N which is in form \"a mod N\" :")
        a = int(input("Enter a: "))
        N = int(input("Enter N: "))
        mul_inv = get_multiplicative_inverse(a,N)
        if mul_inv == False:
            print("GCD of 'a' and 'N' is not 1.")
        else:
            print("Mutiplicative Inverse: " + str(mul_inv))
    else:
        exit(0)

    print("\nPress anything to continue....")
    input()
    if input("Do you want to continue (y/n)").lower() == "y":
        menu()


menu()

