#include <Servo.h>
Servo serv;
int rightA,rightB,leftA,leftB;
int servoPin;


void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  servoPin=2;
  serv.attach(servoPin);
  rightA=3;
  rightB=5;
  leftA=6;
  leftB=11;
  serv.write(130);
}

void right(int input){
  if(input>0){
        analogWrite(rightA,input);
        digitalWrite(rightB,LOW);
      }
      else{
        analogWrite(rightB,-input);
        digitalWrite(rightA,LOW);
      }
}
void left(int input){
  if(input>0){
        analogWrite(leftA,input);
        digitalWrite(leftB,LOW);
      }
      else{
        analogWrite(leftB,-input);
        digitalWrite(leftA,LOW);
      }
}

void loop() {
  // put your main code here, to run repeatedly:
  if(Serial.available()>0){
    int input=Serial.parseInt();
    int letter=Serial.read();
    delay(10);
    switch(letter){
      case 114:right(input);break;
      case 108:left(input);break;
      case 119:right(100);left(100);break;
      case 97:right(100);left(-100);break;
      case 100:right(-100);left(100);break;
      case 115:right(-100);left(-100);break;
      case 122:serv.write(input);break;
      default:Serial.println("? ");
    }
    Serial.println(input);
  }
}
