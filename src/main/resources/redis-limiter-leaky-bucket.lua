local key = KEYS[1]
local capacity = tonumber(ARGV[1])
local interval = tonumber(ARGV[2])
local current = tonumber(redis.call('zscore', key, 'current') or 0)
local now = tonumber(redis.call('time')[1])
redis.call('zremrangebyscore', key, '-inf', now - interval)
local count = tonumber(redis.call('zcard', key) or 0)
if count >= capacity then
    return false
else
    redis.call('zadd', key, now, now)
    return true
end
