program calendario;
uses crt, dos;

const borde = #205#205#205#205#205#205#205#205#205;
const months       : array[0..11] of integer = (31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);

var meses : array[0..11] of integer;
var tecla : integer;
begin
{inicio del programa}
    for i:=0 to 11 do
        begin
            meses[i] := months[i];
        end;
    getdate (year, mes , day, dayofweek);
    dec (mes);
end.