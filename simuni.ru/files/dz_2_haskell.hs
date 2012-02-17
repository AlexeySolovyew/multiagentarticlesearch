--1) a)
maxlist xs = maxlist' (tail xs) (head xs)
maxlist' [] maxx = maxx
maxlist' (x:xs) maxx = if (x>=maxx) then maxlist' xs x else maxlist' xs maxx
--1) b)
--можно сделать эффективнее по аналогии с первой задачкой, но я захотел сделать так
maxsumlist xs = maxlist (constr xs)
--construct [x,y] = [x+y] - почему-то компилятор ругается
constr (x:y:[]) = [x+y]
сonstr (x:y:xs) =[x+y] ++ constr ([y]++xs)
--2)
getlength [] = 0
getlength xs = 1 + getlength (tail xs) 
rev xs = reverse' xs (getlength xs - 1)
reverse' _ (-1) = []
reverse' xs n = [xs !! n] ++ reverse' xs (n-1)
--3)
checkall _ [] = True
checkall p (x:xs) = if (p x == True) then checkall p xs else False
--4)
alldiff (x:[]) = True
alldiff (x:xs) = if (checkelem x xs == True) then alldiff xs else False 
checkelem x [] = True
checkelem x (y:xs) = if (x==y) then False else checkelem x xs   
  


         





