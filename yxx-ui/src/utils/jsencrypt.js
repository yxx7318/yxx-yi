import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAK2Sn4AvAbSvnsphK+h4bQdxkcM7s9xU\n' +
  '7XB2EoRgAwxAW9ASjSfHkFQqktNFIZAJZ1r4N5MdYfvwIq8RgsDG85MCAwEAAQ=='

const privateKey = 'MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEArZKfgC8BtK+eymEr\n' +
  '6HhtB3GRwzuz3FTtcHYShGADDEBb0BKNJ8eQVCqS00UhkAlnWvg3kx1h+/AirxGC\n' +
  'wMbzkwIDAQABAkAJQaN964PUxfTa6Bm2WQ2/cuAKIFn0KxdKgxFzu8cN1R5CJCrS\n' +
  'IVU4bMuFP3SS+D4kMCUjxF9kh/zhYNuri5bBAiEA5GOASb9VdGCyy7da9/SXmfBP\n' +
  '1U8UWPrvlz73d8XMObMCIQDCjpmqAYWRPO8aGGaMwNBuFw+2SX3HrJfYByg/1eWu\n' +
  'oQIgfzrC89Jn644W7I+3WJxke/g4sQ79udgI8su8FngVHisCIDBRYfBKuHU9gNQm\n' +
  'QzGpoRn2ODy5O7efMYbBIEnaQVSBAiA0BSaimpB37IszYy9Vb9LuE3yuZvXNGc3R\n' +
  'vZI8/zNayA=='

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对数据进行加密
}

// 解密
export function decrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(privateKey) // 设置私钥
  return encryptor.decrypt(txt) // 对数据进行解密
}

