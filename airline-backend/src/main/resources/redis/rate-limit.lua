local now = tonumber(ARGV[1])
local windowStart = tonumber(ARGV[2])
local ttl = tonumber(ARGV[3])
local limit = tonumber(ARGV[4])
local requestId = ARGV[5]

redis.call('ZREMRANGEBYSCORE', KEYS[1], '-inf', windowStart)

local count = redis.call('ZCARD', KEYS[1])

if(count >= limit) then
    return 0
end

redis.call('ZADD', KEYS[1], now, requestId)

redis.call('EXPIRE', KEYS[1], ttl)

return 1