<div align="center">

<img src="docs/icon.png" width="112" alt="Rooba">

# Rooba 🦊

یه وی‌پی‌ان رایگان اندروید که روی VPN خود فایرفاکس کار می‌کنه

![Android](https://img.shields.io/badge/Android-8.0%2B-212121?style=for-the-badge&logo=android&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-212121?style=for-the-badge)

</div>

---

## روبا چیه؟

هر اکانت فایرفاکس ماهی **۵۰ گیگ** VPN رایگان داره که معمولاً فقط تو خود مرورگر فایرفاکس کار می‌کنه. روبا همین سهمیه رو برمی‌داره میاره رو کل گوشیتون، یعنی همه‌ی اپا ازش رد میشن.

نه اشتراک می‌خواد نه پول، فقط یه اکانت فایرفاکس.

## چطوری راهش بندازم؟

1. یه اکانت فایرفاکس بسازید: [accounts.firefox.com/signup](https://accounts.firefox.com/signup)
2. آخرین نسخه رو از بخش [Releases](../../releases/latest) دانلود و نصب کنید
3. با ایمیل و رمز اکانت فایرفاکس وارد شید
4. یه لوکیشن انتخاب کنید و دکمه رو بزنید ✅

## چیا داره

- کل گوشی رو وی‌پی‌ان می‌کنه، نه فقط مرورگر
- چند تا لوکیشن مختلف
- اسپلیت تانل، یعنی می‌تونید بگید کدوم اپا از وی‌پی‌ان رد نشن
- DNS رمزنگاری‌شده (DoH)
- حالت فقط پراکسی
- تم تیره و روشن
- چک کردن IP خروجی و لاگ داخل اپ

## چند تا نکته

- سقفش ماهی ۵۰ گیگه. تموم شد تا اول ماه بعد وصل نمیشه، پس دانلودای سنگین رو بیخیال شید
- بهتره **یه اکانت جدا فقط برای روبا** بسازید
- روبا یه اپ مستقله و ربطی به موزیلا نداره
- اندروید ۸ به بالا

## ساختن از سورس

```
git clone https://github.com/Amirmahdavi2022/Rooba.git
cd Rooba
gradle :app:assembleRelease
```

JDK 17، اندروید SDK 35 و NDK `26.1.10909125` لازمه. بیلدای رسمی رو گیت‌هاب اکشن خودش می‌سازه.

---

<sub>کد اولیه از [FoxyVPN](https://github.com/Vauth/FoxyVPN) اومده و تونلش با [hev-socks5-tunnel](https://github.com/heiher/hev-socks5-tunnel) کار می‌کنه. لایسنس MIT</sub>
