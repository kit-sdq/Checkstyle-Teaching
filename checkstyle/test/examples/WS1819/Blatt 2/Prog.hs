module Prog where

x1 :: [Int]
x1 = 1 : (xre 0)
    where
        xre n
            | ((mod (x1!!n) 12) == 0) = ((div (x1!!n) 3) + 1) : (xre (n+1))
            |otherwise = ((x1!!n) * 2 + 4) : (xre (n+1))
            
--1;6;16;36;13;30;64;132;45;94;192;65;134;272;548;1100;2204;4412;8828;17660;35324;70652;141308;282620;565244;1130492;2260988;4521980;9043964;18087932;36175868;72351740;144703484;289406972;578813948

x2 :: [Int]
x2 = 4 : (xre 1)
    where
        xre n
            | (mod n 2 == 1) = (7-3*n) : (xre (n+1))
            |otherwise = (-7+3*n) : (xre (n+1))

--4;4;-1;-2;5;-8;11;-14;17;-20;23;-26;29;-32;35;-38;41;-44;47;-50

x3 :: Int
x3 = sum [45..82]

--2413

x4 :: Int -> Double
x4 n = xre 0 n
    where
        xre i n
            | i <= n = ( (fromIntegral ((-1) ^ i)) / (fromIntegral (2*i+1)) ) + (xre (i+1) n)
            |otherwise = 0

--0.7934605842976874

v :: Int -> Int -> Int -> Double
v bigR r h
    | r >= bigR = error("r must be < bigR")
    |otherwise  = ( fromIntegral ((bigR*bigR) + bigR*r + r*r) ) * ((fromIntegral h)*pi/3.0)

--

o :: Int -> Int -> Int -> Double
o bigR r h
    | r >= bigR = error("r must be < bigR")
    |otherwise  = (pi * (fromIntegral (bigR*bigR))) + (pi * (fromIntegral (r*r))) + (pi * (m bigR r h) * (fromIntegral (bigR + r)))
    
--

am :: Int -> Int -> Int -> Double
am bigR r h
    | r >= bigR = error("r must be < bigR")
    |otherwise  = (fromIntegral (bigR+r)) * pi * (m bigR r h)

--

m :: Int -> Int -> Int -> Double
m bigR r h
    | r >= bigR = error("r must be < bigR")
    |otherwise  = sqrt ( fromIntegral ((bigR-r)*(bigR-r) + h*h) )

--
