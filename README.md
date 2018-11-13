# SliderDateTimePicker
Developed this Date Time Picker as the requirement for [Wheelstreet - Bike Rentals Android App](https://play.google.com/store/apps/details?id=in.wheelstreet)

 
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/sharukhmohammed/SliderDateTimePicker/issues)  [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)   [![MinSdk: 14](https://img.shields.io/badge/minSdk-14-green.svg)](https://developer.android.com/about/versions/android-4.0)   [ ![Download](https://api.bintray.com/packages/sharukhmohammed/SliderDTPicker/sharukh.sliderdtpicker/images/download.svg) ](https://bintray.com/sharukhmohammed/SliderDTPicker/sharukh.sliderdtpicker/_latestVersion)  [![HitCount](http://hits.dwyl.com/sharukhmohammed/SliderDateTimePicker.svg)](http://hits.dwyl.com/sharukhmohammed/SliderDateTimePicker)


![](demo.gif)

This is based off  [Horizontal-Calendar](https://github.com/Mulham-Raee/Horizontal-Calendar) with Time Selector built on top of it

## Installation
Add this line in app level build.gradle file 
```gradle
implementation 'sharukh.sliderdtpicker:sliderdtpicker:latest-version'
```
## Dependencies

```gradle
implementation 'com.android.support:appcompat-v7:28.0.0'
implementation 'com.android.support:design:28.0.0'
```
## Usage

```java

SliderDateTimePicker.newInstance()
                        .setOnDateTimeSetListener((new SliderDateTimePicker.OnDateTimeSetListener() {
                            @Override
                            public void onDateTimeSelected(final Calendar startTime) {

                                SliderDateTimePicker.newInstance()
                                        .setStartDate(startTime.getTime())
                                        .setOnDateTimeSetListener(new SliderDateTimePicker.OnDateTimeSetListener() {
                                            @Override
                                            public void onDateTimeSelected(Calendar endTime) {

                                                textView.setText(sdf.format(startTime.getTime()) + " ---to--- " + sdf.format(endTime.getTime()));

                                            }
                                        })
                                        .setStartLabel("Some Kinda Label")
                                        .setEndLabel("Whatever")
                                        .setTimeTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ongoing_dark))
                                        .setSelectedDateBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.somedrawble))
                                        .setSelectedTimeBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.somedrawble2))
                                        .setDoneButtonBackground(ContextCompat.getDrawable(getApplicationContext(), R.color.failure_dark))
                                        .show(getSupportFragmentManager(), "Your wish");
                            }
                        }))
                        .setStartLabel("Start Time")
                        .show(getSupportFragmentManager(), "Your wish");


```




## Contributions

All contributions in any kind are more than welcome!

[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)   [![forthebadge](https://forthebadge.com/images/badges/built-for-android.svg)](https://forthebadge.com)   [![forthebadge](https://forthebadge.com/images/badges/mom-made-pizza-rolls.svg)](https://forthebadge.com)

<a href='https://www.paypal.me/sharukhmohammed' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi2.png?v=0' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a>


## License (MIT)
```
Copyright 2019 Sharukh Mohammed

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
