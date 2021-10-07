package top.jolyoulu;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/7 11:38
 * @Version 1.0
 */
public class JavassistDemo {

    public static void premain(String args, Instrumentation instrumentation){
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                //拦截top/jolyoulu/AgentServer类的装载
                if ("top/jolyoulu/AgentServer".equals(className)){
                    return hourMeter();
                }
                return new byte[0];
            }
        },true);

    }

    private static byte[] hourMeter(){
        try {
            ClassPool pool = new ClassPool();
            pool.appendSystemPath();
            CtClass ctClass = pool.get("top.jolyoulu.AgentServer");
            //修改指定方法字节码
            CtMethod ctMethod = ctClass.getDeclaredMethod("sayHello");
            //$0、$1、$2：方法参数$0表示this $1第一个参数$2第二个参数，以此类推
            //注：静态方法没有this所以没有$0参数
            ctMethod.insertBefore("System.out.println(this==$0);");
            ctMethod.insertBefore("System.out.println($1);");
            ctMethod.insertBefore("System.out.println($2);");
            ctMethod.insertBefore("System.out.println(\"=================$0,$1,$2===============\");");
            //$args：将参数以Object数组形式进行封装，相当于new Object[]{$1,$2}
            ctMethod.insertBefore("System.out.println(java.util.Arrays.toString($args));");
            ctMethod.insertBefore("System.out.println(\"=================$args===============\");");
            //$$：该方法中传入的所有参数缩写，复杂写法$1,$2
            ctMethod.insertBefore("System.out.println(append($$));");
            ctMethod.insertBefore("System.out.println(\"=================$$===============\");");
            //$sig：获取方法中所有参数类型
            ctMethod.insertBefore("System.out.println(java.util.Arrays.toString($sig));");
            ctMethod.insertBefore("System.out.println(\"=================$sig===============\");");
            //$type：获取方法结果的class
            ctMethod.insertBefore("System.out.println($type);");
            ctMethod.insertBefore("System.out.println(\"=================$type===============\");");
            //$class：获取的确方法所在类的class
            ctMethod.insertBefore("System.out.println($class);");
            ctMethod.insertBefore("System.out.println(\"=================$class===============\");");
            //$w：基础类型转换，如将3转换为int类型 Integer al = ($w)3;
            ctMethod.insertBefore("Integer al = ($w)3;");
            //return ($w)：表示直接return
            //ctMethod.insertBefore("return ($w)3;");
            //ctMethod.insertBefore("System.out.println(\"=================$w===============\");");
            return ctClass.toBytecode();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

}
