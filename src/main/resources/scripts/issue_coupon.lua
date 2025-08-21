

-- couponName, "Stock", "UsedUsers"
-- couponName
-- id

-- 중복 확인
if redis.call("SISMEMBER", KEYS[3], ARGV[2]) == 1 then
    redis.call("HINCRBY", "StockCheck", "D", 1)
    return "DUPLICATE"
end

-- 재고 확인
local stock = redis.call("HGET", KEYS[2], ARGV[1])
if not stock or tonumber(stock) <= 0 then
    redis.call("HINCRBY", "StockCheck", "D", 1)
    return "SOLD_OUT"
end

local entries = redis.call("HGETALL", KEYS[1])
local targetField = nil

-- 넣을 곳 찾기
for i = 1, #entries, 2 do
    if entries[i+1] == "0" then
        targetField = entries[i]
        break
    end
end

-- 넣고 재고 차감, 테스트 재고 ++
if targetField then
    -- 해시 테이블에 이름 넣기
    redis.call("HSET", KEYS[1], targetField, ARGV[2])
    -- 재고에서 --;
    redis.call("HINCRBY", KEYS[2], ARGV[1], -1)

    -- 중복 제거 위해 set에 추가
    redis.call("SADD", KEYS[3], ARGV[2])

    -- 테스트 재고 ++
    redis.call("HINCRBY", "StockCheck", ARGV[1], 1)

    return targetField
else
    -- 혹시나 동시성 이슈 나오나 확인용
    redis.call("HINCRBY", "StockCheck", "D", 1)
    return "AAA"
end