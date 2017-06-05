package type

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Before


/**
 * Created by atyun on 6/5/17.
 */

@Aspect
class ActiveListAop{

    @Pointcut("execution(@annotation.lazy_list * *(..))")
    fun profile() {

    }

    @Before("profile()")
    fun testModeOnly(joinPoint: JoinPoint) {
        println("123")
    }

}