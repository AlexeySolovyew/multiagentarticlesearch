--1 a)
matr n = matr' n 1
matr' n k = if k>n then [] else [makerow n k]++matr' n (k+1)
makerow n k = makerow' n k 1
makerow' n k m = if m>n then [] else if m==k then [1]++makerow' n k (m+1) else [0]++makerow' n k (m+1)
--1 �)
matrb n = matrb' n 1 n
matrb' n k l = if k>n then [] else [makerowb n k l]++matrb' n (k+1) (l-1)
makerowb n k l = makerowb' n k l 1
makerowb' n k l m = if m>n then [] else if (m==k || m==l) then [1]++makerowb' n k l (m+1) else [0]++makerowb' n k l (m+1)
--2 �)
nechsum xs = foldr (\x res -> if x `mod` 2 == 1 then x+res else res) 0 xs
--2 �)
nechsum2 [] = 0
nechsum2 (x:xs) = if x `mod` 2 == 1 then x+nechsum2 xs else nechsum2 xs
--3
myfoldl f e [] = e
myfoldl f e (x:xs) = f (myfoldl f e xs) x
--5
comp f n = foldr . (\x -> x) (map (\x -> f) [1..n])
 






