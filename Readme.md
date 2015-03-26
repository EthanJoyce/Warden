# Warden

[![Join the chat at https://gitter.im/LordLambda/Warden](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/LordLambda/Warden?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

#### What is Warden? ####

**Warden is an open source anti-cheat framework for Spigot (and the will be ported over to Sponge (maybe)). It is meant to be flexible with some regards, and highly highly capable. It will fight bugs that NCP does not, and do it better than any paid link out there. Some of these bugs it attacks that others do not are things like XCarry (carrying more items), ForceField/KillAura(to some extent), [and even a exploit to spawn in any item a player would like (bypassing spawn restricitions, and such) originally revealed to the public by the Lead-Developer][llink].**

#### Contributing to Warden ####

**The great thing about Warden is *anyone* can contribute to it. It can be any sort of changes, from full on structural changes, to minor changes, and even miniscule changes. However there are some rules about contributing to Warden.**

##### Formatting Guidlines: #####
**Warden uses [google code style formatting.][link2] Some examples of this are listed below:**

```java
if(true) {
  System.out.println("Braces are used even where optional.");
}
```

* 2 Spaces instead of a '\t' or 4 spaces.
* No line break before the opening brace.
* Line break after the opening brace.
* Line break before the closing brace.
* Line break after the closing brace if that brace terminates a statement or the body of a method, constructor or named class. For example, there is no line break after the brace if it is followed by else or a comma.
* Column limit is 100 characters.

```java
return new MyClass() {
  @Override public void method() {
    if (condition()) {
      try {
        something();
      } catch (ProblemException e) {
        recover();
      }
    }
  }
};
```

##### Alignment Rules #####

Google code style is quite vague on alignment so heres the perferred characters:

```java
private int x; // this is fine
private Color color; // this too

private int   x;      // This is not okay
private Color color;  // None of this.
```

**If you don't follow these guidlines your PR will be denied immediatly.**

**Next simply fully comment what your change does, and why it's needed. Then submit your PR again describing everything, and wait for approval!**
[llink]: http://lordlambda.ninja/another-mc-exploit/
[link2]: https://google-styleguide.googlecode.com/svn/trunk/javaguide.html
