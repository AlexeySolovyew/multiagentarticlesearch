--1
firstbig = head (filter (>0.9999) (map sin [1..]))
--2 а)
aproxsum xs = aproxsum' 0 xs
aproxsum' sum (x:xs) = if x>=0.0001 then aproxsum' (sum+x) xs else sum 
--2 б)
list1n2 =  [1/(x*x)|x<-[1..]]  
--3
sumhol xs = sumhol' 0 xs
sumhol' sum (x1:x2:x3:x4:x5:x6:x7:xs) = sumhol' (sum+x6+x7) xs 
sumhol' sum _ = sum
--4
kantor n = [(i,j)|i<-[1..n-1],let j = n-i] ++ kantor (n+1)
--сам список пар чисел:
lst = kantor 2                                                            

                           


