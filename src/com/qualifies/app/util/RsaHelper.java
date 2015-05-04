package com.qualifies.app.util;

import android.util.Base64;

import java.io.*;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;

import javax.crypto.Cipher;

public class RsaHelper
{
	/**
	 * ����RSA��Կ��(Ĭ����Կ����Ϊ1024)
	 * 
	 * @return
	 */
	public static KeyPair generateRSAKeyPair()
	{
		return generateRSAKeyPair(1024);
	}

	/**
	 * ����RSA��Կ��
	 * 
	 * @param keyLength ��Կ���ȣ���Χ��512��2048
	 * @return
	 */
	public static KeyPair generateRSAKeyPair(int keyLength)
	{
		try
		{
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA/ECB/PKCS1Padding");
			kpg.initialize(keyLength);
			return kpg.genKeyPair();
		}
		catch (NoSuchAlgorithmException e)
		{
			return null;
		}
	}

	/*
	 * java�˹�Կת����C#��Կ
	 */
	public static String encodePublicKeyToXml(PublicKey key)
	{
		if (!RSAPublicKey.class.isInstance(key))
		{
			return null;
		}
		RSAPublicKey pubKey = (RSAPublicKey) key;
		StringBuilder sb = new StringBuilder();

		sb.append("<RSAKeyValue>");
		sb.append("<Modulus>")
			.append(Base64Helper.encode(pubKey.getModulus().toByteArray()))
			.append("</Modulus>");
		sb.append("<Exponent>")
			.append(Base64Helper.encode(pubKey.getPublicExponent().toByteArray()))
			.append("</Exponent>");
		sb.append("</RSAKeyValue>");
		return sb.toString();
	}

	/*
	 * C#�˹�Կת����java��Կ
	 */
	public static PublicKey decodePublicKeyFromXml(String xml)
	{
		xml = xml.replaceAll("\r", "").replaceAll("\n", "");
		BigInteger modulus =
			new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml,
				"<Modulus>", "</Modulus>")));
		BigInteger publicExponent =
			new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml,
				"<Exponent>", "</Exponent>")));

		RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, publicExponent);

		KeyFactory keyf;
		try
		{
			keyf = KeyFactory.getInstance("RSA");
			return keyf.generatePublic(rsaPubKey);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/*
	 * C#��˽Կת����java˽Կ
	 */
	public static PrivateKey decodePrivateKeyFromXml(String xml)
	{
		xml = xml.replaceAll("\r", "").replaceAll("\n", "");
		BigInteger modulus =
			new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml,
				"<Modulus>", "</Modulus>")));
		BigInteger publicExponent =
			new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml,
				"<Exponent>", "</Exponent>")));
		BigInteger privateExponent =
			new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml, "<D>",
				"</D>")));
		BigInteger primeP =
			new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml, "<P>",
				"</P>")));
		BigInteger primeQ =
			new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml, "<Q>",
				"</Q>")));
		BigInteger primeExponentP =
			new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml,
				"<DP>", "</DP>")));
		BigInteger primeExponentQ =
			new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml,
				"<DQ>", "</DQ>")));
		BigInteger crtCoefficient =
			new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml,
				"<InverseQ>", "</InverseQ>")));

		RSAPrivateCrtKeySpec rsaPriKey =
			new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP,
				primeQ, primeExponentP, primeExponentQ, crtCoefficient);

		KeyFactory keyf;
		try
		{
			keyf = KeyFactory.getInstance("RSA");
			return keyf.generatePrivate(rsaPriKey);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/*
	 * java��˽Կת����C#˽Կ
	 */
	public static String encodePrivateKeyToXml(PrivateKey key)
	{
		if (!RSAPrivateCrtKey.class.isInstance(key))
		{
			return null;
		}
		RSAPrivateCrtKey priKey = (RSAPrivateCrtKey) key;
		StringBuilder sb = new StringBuilder();

		sb.append("<RSAKeyValue>");
		sb.append("<Modulus>")
			.append(Base64Helper.encode(priKey.getModulus().toByteArray()))
			.append("</Modulus>");
		sb.append("<Exponent>")
			.append(Base64Helper.encode(priKey.getPublicExponent().toByteArray()))
			.append("</Exponent>");
		sb.append("<P>").append(Base64Helper.encode(priKey.getPrimeP().toByteArray()))
			.append("</P>");
		sb.append("<Q>").append(Base64Helper.encode(priKey.getPrimeQ().toByteArray()))
			.append("</Q>");
		sb.append("<DP>")
			.append(Base64Helper.encode(priKey.getPrimeExponentP().toByteArray()))
			.append("</DP>");
		sb.append("<DQ>")
			.append(Base64Helper.encode(priKey.getPrimeExponentQ().toByteArray()))
			.append("</DQ>");
		sb.append("<InverseQ>")
			.append(Base64Helper.encode(priKey.getCrtCoefficient().toByteArray()))
			.append("</InverseQ>");
		sb.append("<D>")
			.append(Base64Helper.encode(priKey.getPrivateExponent().toByteArray()))
			.append("</D>");
		sb.append("</RSAKeyValue>");
		return sb.toString();
	}

	// �ù�Կ����
	public static byte[] encryptData(byte[] data, PublicKey pubKey)
	{
		try
		{
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			return cipher.doFinal(data);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	// ��˽Կ����
	public static byte[] decryptData(byte[] encryptedData, PrivateKey priKey)
	{
		try
		{
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, priKey);
			return cipher.doFinal(encryptedData);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * ����ָ����Կ�������ļ���
	 * 
	 * @param plainText Ҫ���ܵ���������
	 * @param pubKey ��Կ
	 * @return
	 */
	public static String encryptDataFromStr(String plainText, PublicKey pubKey)
	{

		try
		{
			byte[] dataByteArray = plainText.getBytes("UTF-8");
			byte[] encryptedDataByteArray = RsaHelper.encryptData(dataByteArray, pubKey);
			return Base64Helper.encode(encryptedDataByteArray);
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * ����ָ��˽Կ�����ݽ���ǩ��(Ĭ��ǩ���㷨Ϊ"SHA1withRSA")
	 * 
	 * @param data Ҫǩ��������
	 * @param priKey ˽Կ
	 * @return
	 */
	public static byte[] signData(byte[] data, PrivateKey priKey)
	{
		return signData(data, priKey, "SHA1withRSA");
	}

	/**
	 * ����ָ��˽Կ���㷨�����ݽ���ǩ��
	 * 
	 * @param data Ҫǩ��������
	 * @param priKey ˽Կ
	 * @param algorithm ǩ���㷨
	 * @return
	 */
	public static byte[] signData(byte[] data, PrivateKey priKey, String algorithm)
	{
		try
		{
			Signature signature = Signature.getInstance(algorithm);
			signature.initSign(priKey);
			signature.update(data);
			return signature.sign();
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * ��ָ���Ĺ�Կ����ǩ����֤(Ĭ��ǩ���㷨Ϊ"SHA1withRSA")
	 * 
	 * @param data ����
	 * @param sign ǩ�����
	 * @param pubKey ��Կ
	 * @return
	 */
	public static boolean verifySign(byte[] data, byte[] sign, PublicKey pubKey)
	{
		return verifySign(data, sign, pubKey, "SHA1withRSA");
	}

	/**
	 * @param data ����
	 * @param sign ǩ�����
	 * @param pubKey ��Կ
	 * @param algorithm ǩ���㷨
	 * @return
	 */
	public static boolean verifySign(byte[] data, byte[] sign, PublicKey pubKey,
			String algorithm)
	{
		try
		{
			Signature signature = Signature.getInstance(algorithm);
			signature.initVerify(pubKey);
			signature.update(data);
			return signature.verify(sign);
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	public static PublicKey loadPublicKey(String publicKeyStr) throws Exception
	{
		try
		{
			byte[] buffer = Base64Helper.decode(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e)
		{
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e)
		{
			throw new Exception("公钥非法");
		} catch (NullPointerException e)
		{
			throw new Exception("公钥数据为空");
		}
	}

	public static PublicKey loadPublicKey(InputStream in) throws Exception
	{
		try
		{
			return loadPublicKey(readKey(in));
		} catch (IOException e)
		{
			throw new Exception("公钥数据流读取错误");
		} catch (NullPointerException e)
		{
			throw new Exception("公钥输入流为空");
		}
	}

	/**
	 * 从字符串中加载私钥<br>
	 * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
	 *
	 * @param privateKeyStr
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception
	{
		try
		{
			byte[] buffer = Base64Helper.decode(privateKeyStr);
			// X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e)
		{
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e)
		{
			throw new Exception("私钥非法");
		} catch (NullPointerException e)
		{
			throw new Exception("私钥数据为空");
		}
	}


	/**
	 * 从文件中加载私钥
	 *
	 * @param keyFileName
	 *            私钥文件名
	 * @return 是否成功
	 * @throws Exception
	 */
	public static PrivateKey loadPrivateKey(InputStream in) throws Exception
	{
		try
		{
			return loadPrivateKey(readKey(in));
		} catch (IOException e)
		{
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e)
		{
			throw new Exception("私钥输入流为空");
		}
	}


	/**
	 * 读取密钥信息
	 *
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static String readKey(InputStream in) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String readLine = null;
		StringBuilder sb = new StringBuilder();
		while ((readLine = br.readLine()) != null)
		{
			if (readLine.charAt(0) == '-')
			{
				continue;
			} else
			{
				sb.append(readLine);
				sb.append('\r');
			}
		}

		return sb.toString();
	}

}
