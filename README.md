__Step 1.__ Add the JitPack repository to your build file

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
    

__Step 2.__ Add the dependency    
    
    dependencies {
            implementation 'com.github.kisoojo:ksbaseapp:1.0'
    }

